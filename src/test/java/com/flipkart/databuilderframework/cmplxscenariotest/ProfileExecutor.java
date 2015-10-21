package com.flipkart.databuilderframework.cmplxscenariotest;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

//wrapper to measure context switch time
public class ProfileExecutor implements ExecutorService {

	private final  ExecutorService exec;
	private final int threshold;
	private final AtomicInteger rejectionCounter = new AtomicInteger(0);
	private final AtomicInteger maxLatency = new AtomicInteger(0);
	private final AtomicInteger contextSwitchCounter = new AtomicInteger(0);
	
	
	public synchronized void resetStats(){
		this.rejectionCounter.set(0);
		this.maxLatency.set(0);
		this.contextSwitchCounter.set(0);
	}
	
	public int getRejectedCount(){
		return this.rejectionCounter.get();
	}
	
	public int getNumberOfContextSwitchesOverThresHold(){
		return this.contextSwitchCounter.get();
	}
	
	public int getMaxContextSwitchLatency(){
		return this.maxLatency.get();
	}
	
	public ProfileExecutor(int size, int queueSize,int ctxSwitchTheshold) {
		this.threshold = ctxSwitchTheshold;
		LinkedBlockingQueue<Runnable> queue = (queueSize == -1) ?  new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(queueSize);
		this.exec = new ThreadPoolExecutor(size, size, 0, TimeUnit.SECONDS, 
				queue,
				new ThreadFactory() {
			int i=0;
			@Override
			public Thread newThread(Runnable r) {
				Thread th = new Thread(r,"builder-"+(i++));
				return th;
			}
		}, new RejectedExecutionHandler() {

			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				rejectionCounter.incrementAndGet(); // not the safest but will give some idea
			}
		});
	}

	@Override
	public void execute(Runnable command) {
		final Long start = System.currentTimeMillis();
		final Runnable runnableRef = command;
		exec.execute(new Runnable() {
			@Override
			public void run() {
				final int duration = (int) (System.currentTimeMillis()-start);
				if(duration > threshold){
					contextSwitchCounter.incrementAndGet();
					int oldVal,newVal;
					do{
						 oldVal = maxLatency.get();
						if(duration > oldVal){
							newVal = duration;
						}else{
							newVal = oldVal;
						}
					}while(!maxLatency.compareAndSet(oldVal, newVal));
				}
				runnableRef.run();
			}
		});
	}

	@Override
	public void shutdown() {
		exec.shutdown();

	}

	@Override
	public List<Runnable> shutdownNow() {
		return exec.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return exec.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return exec.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return exec.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		final Long start = System.currentTimeMillis();
		final Callable<T> taskref = task;
		return exec.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				System.out.println("context switch time "+(System.currentTimeMillis()-start));
				return taskref.call();
			}
		});
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return null;
	}

	@Override
	public Future<?> submit(Runnable task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
					throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
			long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}
