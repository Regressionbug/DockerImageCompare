package bishe;

/**
 * @author Lixuhang
 * @date 2021/12/19
 * @两个节点比较之后会有两个结论，第一个结论是就是目标点，第二个是如果不是目标点，则判断是否还能继续
 */
public class NodeCompareResult {
    private boolean isContinue;
    private boolean isTarget;

    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setTarget(boolean target) {
        isTarget = target;
    }
}
