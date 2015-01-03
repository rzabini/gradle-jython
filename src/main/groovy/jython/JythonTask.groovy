package jython
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.JavaExec
class JythonTask extends JavaExec {
		def pythonPath=[]
		
		JythonTask(){
			main 'org.python.util.jython'
			classpath project.configurations.jython.asPath
		}
		
		@TaskAction
		void exec(){
			
			
			logger.info "pythonpath:"
			logger.info project.configurations.pythonpath.asPath
			
			systemProperties(["python.path":project.configurations.pythonpath.asPath])
			super.exec()
		}
		
		void script(script){
			if(script instanceof File)
				args script
			else
				args "-c", script
		}
		
}