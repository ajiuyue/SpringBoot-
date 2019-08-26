package cn.jiuyue;

import java.util.LinkedList;
import java.util.Queue;

class BiTree {
    private int n;      //数据域
    private BiTree l;   //左子树
    private BiTree r;   //右子树

    public BiTree(int n) {
        this.n = n;
    }
    //添加子树
    public void add(BiTree x) {
        //如果添加的树的值小于树根的值，加到左子树
        if (x.n < n) {
            //若左子树为空，则加上
            if (l == null) {
                l = x;
            }
            //左子树不空，则将左子树作为树根，继续添加
            else {
                l.add(x);
            }
        }
        else {
            if (r == null) {
                r = x;
            } else {
                r.add(x);
            }
        }
    }

    //中序遍历
    public void midTravel() {
        if (l != null) {
            l.midTravel();
        }
        System.out.print(n + " ");
        if (r != null) {
            r.midTravel();
        }
    }
    //层序遍历
    public void levTravel() {
        Queue<BiTree> que = new LinkedList<BiTree>();
        BiTree root = this;
        que.add(root);
        while(!que.isEmpty()) {
            root = que.remove();
            System.out.print(root.n + " ");
            if(root.l != null) {
                que.add(root.l);
            }
            if(root.r != null) {
                que.add(root.r);
            }
        }

    }
    //计算树高
    public int getHeight() {
        int hl, hr;
        if (l == null)
            hl = 0;
        else
            hl = l.getHeight();
        if (r == null)
            hr = 0;
        else
            hr = r.getHeight();
        return Math.max(hl, hr) + 1;
    }


    public void exchange(BiTree biTree) {
        BiTree temp = null;
        if (biTree == null) {
            return;
        } else {
            temp = biTree.r;
            biTree.r = biTree.l;
            biTree.l = temp;
            exchange(biTree.l);
            exchange(biTree.r);
        }
    }
}