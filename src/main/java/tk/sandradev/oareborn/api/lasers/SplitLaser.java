package tk.sandradev.oareborn.api.lasers;

public class SplitLaser {
    public ILaser l1;
    public ILaser l2;
    public SplitLaser(ILaser l1, ILaser l2)
    {
        this.l1 = l1;
        this.l2 = l2;
    }
}
