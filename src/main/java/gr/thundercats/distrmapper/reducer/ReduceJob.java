package gr.thundercats.distrmapper.reducer;

import java.util.ArrayList;
import java.util.List;

public class ReduceJob<T> {

    private int jobId;
    private int expectedSize;
    private List<T> data;

    public ReduceJob(int jobId, int expectedSize) {
        this.jobId = jobId;
        this.expectedSize = expectedSize;
        this.data = new ArrayList<T>();
    }

    public void dataPush(T data) {
        this.data.add(data);
    }

    public int getJobId() {
        return jobId;
    }

    public List<T> getData() {
        return data;
    }

    public int getExpectedSize() {
        return expectedSize;
    }

    public void setExpectedSize(int expectedSize) {
        this.expectedSize = expectedSize;
    }

    public boolean canProcess() {
        return data.size() == expectedSize;
    }

    public void restartJob(int poolSize) {
        this.expectedSize = poolSize;
        this.data = new ArrayList<T>();
    }
}
