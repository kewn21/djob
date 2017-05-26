/**
 * This file created at Jan 13, 2014.
 *
 */
package org.kesy.djob.sdu.api;

import java.util.Map;

import org.kesy.djob.sdu.api.model.ListenerParam;
import org.kesy.djob.sdu.api.task.TaskExecutor;

/**
 * 类<code>{@link JobListener}</code>  创建于 Jan 13, 2014<br/>
 * 类描述: {@link JobWrapper}的执行、停止方法在进行调度前、后、异常的处理接口<p>
 * <li></li>
 * @author kewn
 */
public interface JobListener {
	
	void init(Map<String, String> params);

	/**
	 * 功能描述: {@link TaskExecutor#run}调用前处理接口
	 * @param param
	 * @return 是否处理成功，可以控制目标方法是否允许调用（未实现）
	 * @author kewn
	 */
	boolean processExecuteBefore(ListenerParam param);

	/**
	 * 功能描述: {@link TaskExecutor#run}调用后处理接口
	 * @param param 
	 * @return 是否处理成功，可以控制目标方法是否允许调用（未实现）
	 * @author kewn
	 */
	boolean processExecuteAfter(ListenerParam param);

	/**
	 * 功能描述: {@link JobWrapper#execute}异常处理接口
	 * @param param 
	 * @author kewn
	 */
	boolean processExecuteException(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteBefore(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processStopBefore(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteAfter(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processStopAfter(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteException(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processStopException(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteBefore(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processShutdownBefore(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteAfter(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processShutdownAfter(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteException(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processShutdownException(ListenerParam param);
	
	
	/**
	 * 功能描述: 参考{@link JobListener#processExecuteBefore(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processInitBefore(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteAfter(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processInitAfter(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processExecuteException(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processInitException(ListenerParam param);
	
	/**
	 * 功能描述: 参考{@link JobListener#processWaitBefore(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processWaitBefore(ListenerParam param);
	
	/**
	 * 功能描述: 参考{@link JobListener#processWaitAfter(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processWaitAfter(ListenerParam param);

	/**
	 * 功能描述: 参考{@link JobListener#processWaitException(Object[])}
	 * @param param
	 * @return
	 * @author kewn
	 */
	boolean processWaitException(ListenerParam param);

}
