package network.httpclient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolManager {
	private volatile static ThreadPoolManager instance =null;
	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_POOL_SIZE = 30;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "ThreadPoolManager #"
					+ mCount.getAndIncrement());
		}
	};
	private final BlockingQueue<Runnable> mPoolWorkQueue = new LinkedBlockingDeque<Runnable>();
	private final ThreadPoolExecutor mThreadPoolExecutor;

	private ThreadPoolManager() {
		mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
				MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
				mPoolWorkQueue, sThreadFactory);
	}
	
	public static ThreadPoolManager getInstance(){
		if(instance == null){
			synchronized (ThreadPoolManager.class) {
				if(instance == null)
					instance = new ThreadPoolManager();
			}
		}
		return instance;
	}

	public ThreadPoolExecutor getThreadPool() {
		return mThreadPoolExecutor;
	}
}
