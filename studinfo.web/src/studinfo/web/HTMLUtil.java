package studinfo.web;

import java.util.Map;
//класс HTML функций
public class HTMLUtil {
	private StringBuilder start = new StringBuilder();//переменная для генерации кода

	public enum HTMLFormInputType {
		//создаем перечисляемые типы
		//Текстовое поле
		TEXT("text"),
		//Скрытое поле
		HIDDEN("hidden"),
		//Кнопка отправки
		SUBMIT("submit"), 
		//
		SECRET("secret");
		private String type;

		private HTMLFormInputType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

	}
		//Создание табоицы html
	public class HTMLTable {
		private StringBuilder table = new StringBuilder();
			//добавление начала таблицы 
		public HTMLTable() {
			table.append("<table border=1>");
		}
		//создание тега tr
		public HTMLTable tr() {
			table.append("<tr>");
			return this;//возвращает модифицированый объект
		}
		//тег th
		public HTMLTable th(String s) {
			table.append("<th>" + s);
			return this;//возвращает модифицированый объект
		}
		//тег td
		public HTMLTable td(String s) {
			table.append("<td>" + s);
			return this;//возвращает модифицированый объект
		}
		//конец таблицы
		public HTMLUtil done() {
			HTMLUtil.this.start.append(table.toString());//добавляем весь текст что накопился 
			//с помощью методов
			HTMLUtil.this.start.append("</table>");//закрываем таблицу
			table = new StringBuilder();//отчистка
			return HTMLUtil.this;//возвращаем 
		}
	}
//Класс для работы с формами
	public class HTMLForm {

		private StringBuilder form = new StringBuilder();

		public HTMLForm(String action, String method) {
			form.append("<form action=\"" + action + "\" method=\"" + method
					+ "\">");
		}

		public HTMLForm input(HTMLFormInputType type, String name, String value) {
			form.append("<input type=" + type.getType() + " name=\"" + name
					+ "\" value=\"" + value + "\" />");
			return this;
		}
			
		public HTMLForm echo(String s) {
			form.append(s);
			return this;
		}
		//Тег select
		public HTMLForm select(String name, Map<String, Object> values) {
			form.append("<select name=\"" + name + "\">");
			for (String key : values.keySet()) {
				form.append("<option value=\"" + values.get(key) + "\">" + key
						+ "</option>");
			}
			form.append("</select>");
			return this;

		}

		public HTMLUtil done() {
			HTMLUtil.this.start.append(form.toString());
			HTMLUtil.this.start.append("</form>");
			form = new StringBuilder();
			return HTMLUtil.this;
		}
	}

	public HTMLUtil html() {
		start.append("<html>");
		return this;
	}

	public HTMLUtil title(String s) {
		start.append("<head><title>" + s + "</title></head><body>");
		return this;
	}

	public HTMLUtil echo(String s) {
		start.append(s);
		return this;
	}

	public HTMLUtil br() {
		start.append("<BR/>");
		return this;
	}

	public HTMLUtil nbsp() {
		start.append("&nbsp;");
		return this;
	}

	public HTMLUtil a(String url, String title) {
		start.append("<a href=\"" + url + "\">" + title + "</a>");
		return this;
	}

	public HTMLForm form(String action, String method) {
		HTMLForm form = new HTMLForm(action, method);
		return form;
	}

	public HTMLTable table() {
		HTMLTable table = new HTMLTable();
		return table;
	}

	public String done() {
		start.append("</body></html>");
		String s = start.toString();
		start = new StringBuilder();
		return s;
	}

}