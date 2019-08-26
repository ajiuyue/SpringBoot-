package cn.jiuyue;

/**
 * @ClassName BiTreeTest
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/8/26 15:25
 * @Version 3.0
 **/

public class BiTreeTest {

    public static void main(String[] args) {
        BiTree original = new BiTree(4);
        original.add(new BiTree(2));
        original.add(new BiTree(7));
        original.add(new BiTree(1));
        original.add(new BiTree(3));
        original.add(new BiTree(6));
        original.add(new BiTree(9));
        original.midTravel();
        System.out.println();
        original.exchange(original);
        original.midTravel();
        System.out.println();
    }

}