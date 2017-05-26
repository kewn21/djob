/**
 * This file created at 2014-2-27.
 *

 */
package org.kesy.djob.dex.datax.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>{@link ObjectUtils}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class ObjectUtils {
	private static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

	public static Object getFieldValue(Object aObject, String aFieldName,
			Object defaultValue) {

		Object res = getFieldValuePair(aObject).get(aFieldName);

		// Field field = getClassField(aObject.getClass(), aFieldName);// get
		// the
		if (res != null) {
			return res;
		} else {
			return defaultValue;
		}
	}

	public static Map<String, String> getFieldValuePair(Object o) {
		Map<String, String> res = new HashMap<String, String>();
		Field[] field = o.getClass().getDeclaredFields();
		res = getField(field, o, res);
		Class<?> clazz = o.getClass().getSuperclass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			field = clazz.getDeclaredFields();
			res = getField(field, o, res);
		}
		return res;
	}

	private static Map<String, String> getField(Field[] field, Object model,
			Map<String, String> res) {
		logger.info(String.format("%-20s %-15s %-50s  %s", "fieldname",
				"value", "type", "className"));
		for (int i = 0; i < field.length; i++) {
			String type = field[i].getGenericType().toString();
			String name = field[i].getName();

			Object value = null;
			try {
				if (type.equals("class java.lang.String")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = (String) m.invoke(model);

				}
				if (type.equals("class java.lang.Integer")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = (Integer) m.invoke(model);

				}
				if (type.equals("class java.lang.Short")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = (Short) m.invoke(model);

				}
				if (type.equals("class java.lang.Double")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = (Double) m.invoke(model);

				}
				if (type.equals("class java.lang.Boolean")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = (Boolean) m.invoke(model);

				}
				if (type.equals("class java.util.Date")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = (Date) m.invoke(model);

				}

				/*if (type
						.equals("class org.kesy.djob.dex.core.model.PluginName")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					value = ((PluginName) m.invoke(model)).getValue();
				}*/

				// builder
				/*if (type
						.equals("class org.kesy.djob.dex.datax.core.modle.DataxSourcePlusParam")) {
					Method m = model.getClass().getMethod(
							"get" + name.substring(0, 1).toUpperCase()
									+ name.substring(1));
					DataxCommonParam commParam = ((DataxSourcePlusParam) m
							.invoke(model)).getCommonParam();
					res = getField(commParam.getClass().getDeclaredFields(),
							commParam, res);
				}*/

			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
			res.put(name, (value == null ? "" : value.toString()));
			logger.info(String.format("%-20s %-15s %-50s  %s",
					(name == null ? "null" : name), (value == null ? "" : value
							.toString()), type, model.getClass().getName()));
		}
		logger
				.info("------------------------------------------------------------------------");
		return res;
	}
}
