package com.github.effective.java.training.method.equal;

/**
 * @author BirdSnail
 * @date 2019/10/23
 */
public class OverrideEqualTest {


    public final static class CaseInsensitiveString{
        private final String s;
        public CaseInsensitiveString(String s) {
            this.s = s;
        }

        /**
         * 重写的equal方法忽略了大小写
         * 笔记：
         *    不要扯上其他类，不然很容易违反对称性
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CaseInsensitiveString) {
                return s.equalsIgnoreCase(((CaseInsensitiveString) obj).s);
            }

            // 删除与String的交互可以使程序满足对称性
            if (obj instanceof String) {
                return s.equalsIgnoreCase((String) obj);
            }

            return false;
        }
    }

    public static void main(String[] args) {
        String s = "Yang";
        CaseInsensitiveString cis = new CaseInsensitiveString("yang");
        System.out.println(s.equals(cis));
        System.out.println(cis.equals(s)); // 不满足对称性
    }
}
