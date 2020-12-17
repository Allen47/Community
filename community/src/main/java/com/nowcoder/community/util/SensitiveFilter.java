package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Msq
 * @date 2020/12/16 - 10:59
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 根节点
    private TrieNode root = new TrieNode();

    // 替换符
    private static final String REPLACEMENT = "***";

    // 在项目构建好以后立刻读取敏感词文件，初始化前缀树
    @PostConstruct
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ){
            String keyword;
            while((keyword=reader.readLine()) != null){
                // 将每个敏感词添加到前缀树中
                this.addKeyword(keyword);
            }

        }catch(IOException e){
            logger.error(e.getMessage());
        }

    }

    // 添加敏感词到前缀树的方法
    private void addKeyword(String keyword) {
        TrieNode tempNode = root;
        for(int i=0 ; i<keyword.length() ; i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            // tempNode 下移，继续寻找
            tempNode = subNode;

            // 停止标志
            if(i == keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }

    }

    /**
     * 过滤敏感词
     * @param text: 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        // 指针1
        TrieNode tempNode = root;
        //
        int begin = 0; int end = 0;

        // 结果
        StringBuilder sb = new StringBuilder();

        while(end < text.length()){
            char c = text.charAt(end);

            // 跳过符号
            if(isSymbol(c)){
                // 如果指针1在根节点，则直接添加到sb中
                if(tempNode == root){
                    sb.append(c);
                    begin++;
                }

                // 无论符号在开头还是中间，end都要往后走
                end++;
                continue;

            }

            // 检查非符号字符
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){ // 以 begin 开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 后移
                end = ++begin;
                // 重新指到根节点
                tempNode = root;
            } else if(tempNode.isKeywordEnd()){  // begin-end 是敏感词
                sb.append(REPLACEMENT);
                begin = ++end;
                tempNode = root;
            } else {
                // 检查下一个字符
                end++;
            }

        }

        // 循环结束时意味着:end 移动到最末尾了,敏感词已经被检测完了,直接append余下所有字符即可
        sb.append(text.substring(begin));

        return  sb.toString();

    }

    // 判断是否是符号
    private boolean isSymbol(char c){
        // 0x2E80~0x9FFF 是东亚文字范围 --> 在此范围之外且非字母/数字才算特殊符号
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode{

        // 标识此单词是否结束
        private boolean isKeywordEnd = false;

        // 标识子节点-树
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd(){ return  isKeywordEnd; }

        public void setKeywordEnd(boolean isEnd) { this.isKeywordEnd = isEnd; }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) { subNodes.put(c, node); }

        // 获取子节点
        public TrieNode getSubNode(Character c) { return subNodes.get(c); }


    }


}
