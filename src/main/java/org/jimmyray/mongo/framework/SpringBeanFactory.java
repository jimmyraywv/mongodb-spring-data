package org.jimmyray.mongo.framework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Utility bean to retrieve other spring beans from non spring aware classes.
 * Note that once one of the instances of this been is initialized, all the
 * other ones are too as the spring context is stored at the class level.
 * Use with care because of that feature, if reinitialized with a different
 * context unexpected behavior may happen...
 */
public final class SpringBeanFactory implements ApplicationContextAware {
  private static ApplicationContext staticContext;

  /**
   * Set the context through one instance, invoked automatically when the
   * instance is created by instantiation by a Spring application context.
   * @param context the new context to be used to retrieve beans for all
   * instances of this class.
   */
  public void setApplicationContext(final ApplicationContext context) {
    setStaticContext(context);
  }

  private static synchronized void setStaticContext(
      final ApplicationContext context) {
    staticContext = context;
  }

  /**
   * Retrieve a bean by its name.
   * @param beanName the bean name
   * @return the object registered in the spring application context with the
   * specified name.
   */
  public static Object getBean(final String beanName) {
    Object obj = null;
    if (staticContext != null) {
      obj = staticContext.getBean(beanName);
    }
    return obj;
  }

  /**
   * Initializes the static context explicitly by looking up all the xml files
   * in the /context package on the classpath.
   * In case of use outside the web container (EJB)
   * we need to handle manually pulling the context.
   */
  public static synchronized void initContext(){
    if (staticContext == null) {
      setStaticContext(new ClassPathXmlApplicationContext(
          "classpath*:/context/main.xml"));
    }
  }
}
