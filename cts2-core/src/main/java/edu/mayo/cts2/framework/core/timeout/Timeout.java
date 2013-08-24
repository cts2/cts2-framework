package edu.mayo.cts2.framework.core.timeout;

import java.util.concurrent.ConcurrentHashMap;

public class Timeout
{
	private static ConcurrentHashMap<Long, Boolean> timedOutThreads = new ConcurrentHashMap<Long, Boolean>();
	
	public static boolean isTimeLimitExceeded()
	{
		return isTimeLimitExceeded(Thread.currentThread().getId());
	}
	
	public static boolean isTimeLimitExceeded(Long threadId)
	{
		Boolean b = timedOutThreads.get(threadId);
		return (b == null ? false : b.booleanValue());
	}
	
	public static void setTimeLimitExceeded(Long threadId)
	{
		timedOutThreads.put(threadId, true);
	}
	
	public static void clearThreadFlag(Long threadId)
	{
		timedOutThreads.remove(threadId);
	}
}
