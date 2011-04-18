package com.aptana.core.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.aptana.core.CorePlugin;

/**
 * A special subclass of IStatus that we can cast to and grab the stdout or stderr explicitly in cases where that is
 * needed. Otherwise it defaults to old behavior of returning stdout, and using stderr if exit code is not 0 and stdout
 * is empty. Please note that there is still no easy way to get combined stdout/stderr in chronologically like you'd get
 * with {@link ProcessBuilder#redirectErrorStream(boolean)}
 * 
 * @author cwilliams
 */
public class ProcessStatus extends Status
{

	private String stdout;
	private String stderr;

	public ProcessStatus(int exitCode, String stdout, String stderr)
	{
		super(exitCode == 0 ? IStatus.OK : IStatus.ERROR, CorePlugin.PLUGIN_ID, exitCode, generateMessage(exitCode,
				stdout, stderr), null);
		this.stdout = stdout;
		this.stderr = stderr;
	}

	private static String generateMessage(int exitCode, String stdOut, String stderr)
	{
		if (exitCode != 0 && (stdOut == null || stdOut.trim().length() == 0))
		{
			return stderr;
		}
		// TODO We probably shouldn't be removing the newline automatically here.
		if (stdOut != null && stdOut.endsWith("\n")) //$NON-NLS-1$
		{
			return stdOut.substring(0, stdOut.length() - 1);
		}
		return stdOut;
	}

	public String getStdErr()
	{
		return this.stderr;
	}

	public String getStdOut()
	{
		return this.stdout;
	}

}
