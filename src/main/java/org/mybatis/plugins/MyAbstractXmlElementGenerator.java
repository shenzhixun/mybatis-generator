package org.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.sql.JDBCType;

public class MyAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

    private  CodeGeneratorConfig config = null;

    public MyAbstractXmlElementGenerator(CodeGeneratorConfig config) {
        this.config = config;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        // 增加base_column对应sql
        parentElement.addElement(addBaseColumn());
        parentElement.addElement(addBaseWhere());

        // 公用select
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        TextElement selectText = new TextElement(sb.toString());

        sb.setLength(0);
        sb.append("from ").append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sb.append(" T ");
        TextElement fromText = new TextElement(sb.toString());

        XmlElement baseColInclude = new XmlElement("include");
        baseColInclude.addAttribute(new Attribute("refid", config.MAPPER_NAME_BASE_COLUMN));

        XmlElement baseWhereInclude = new XmlElement("include");
        baseWhereInclude.addAttribute(new Attribute("refid", config.MAPPER_NAME_BASE_WHERE_CLAUSE));

        XmlElement pageList = new XmlElement("select");
        pageList.addAttribute(new Attribute("id", config.METHOD_LISTBYPAGE));
        pageList.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        pageList.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));

        pageList.addElement(selectText);
        pageList.addElement(baseColInclude);
        pageList.addElement(fromText);
        pageList.addElement(baseWhereInclude);
        parentElement.addElement(pageList);

    }





    /**
     * 判断是否为空的条件语句
     * @param column 字段名
     * @return
     */
    public static XmlElement selectNotNull(String column) {
        StringBuffer buffer = new StringBuffer();
        XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$




        return selectNotNullElement;
    }

    /**
     * 判断是否为空的条件语句
     * @return
     */
    public static XmlElement selectNotEmpty(IntrospectedColumn introspectedColumn) {
        StringBuffer sb = new StringBuffer();
        XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
        sb.setLength(0);
        sb.append("null != ");
        sb.append(introspectedColumn.getJavaProperty());
        selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
        sb.setLength(0);
        // 添加and
        sb.append(" and ");
        // 添加别名t
        sb.append("T.");
        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        // 添加等号
        sb.append(" = ");
        sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

        return selectNotNullElement;
    }

    private static String notNull(IntrospectedColumn introspectedColumn) {
        StringBuffer sb = new StringBuffer();
        sb.append("null != ").append(introspectedColumn.getJavaProperty());
        if(JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.BIT ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.SMALLINT ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.TINYINT ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.INTEGER ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.BIGINT ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.FLOAT ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.DOUBLE ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.NUMERIC ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.DECIMAL ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.ROWID ||
                JDBCType.valueOf(introspectedColumn.getJdbcType())==JDBCType.REAL
        ) {

        } else {
            sb.append(" and ");
            sb.append("'' != ").append(introspectedColumn.getJavaProperty());
        }
        return sb.toString();
    }



    private XmlElement addBaseColumn() {
        // 增加base_query
        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", CodeGeneratorConfig.MAPPER_NAME_BASE_COLUMN));
        StringBuilder sb = new StringBuilder();
        for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            // 添加别名t
            sb.append("T.");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            // 添加等号
            sb.append(", ");
            //去掉最后一个
        }
        //sb.subSequence(0, sb.toString().lastIndexOf(","));
        sql.addElement(new TextElement(sb.toString().substring(0, sb.toString().lastIndexOf(","))));
        return sql;
    }

    private XmlElement addBaseWhere() {
        // 增加base_query
        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", CodeGeneratorConfig.MAPPER_NAME_BASE_WHERE_CLAUSE));
        //在这里添加where条件
        XmlElement selectTrimElement = new XmlElement("trim"); //设置trim标签
        selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));
        selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR")); //添加where和and
        StringBuilder sb = new StringBuilder();
        for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            selectNotNullElement.addAttribute(new Attribute("test", notNull(introspectedColumn)));
            sb.setLength(0);
            // 添加and
            sb.append(" and ");
            // 添加别名t
            sb.append("T.");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            // 添加等号
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            selectNotNullElement.addElement(new TextElement(sb.toString()));
            selectTrimElement.addElement(selectNotNullElement);
        }
        sql.addElement(selectTrimElement);

        return sql;
    }



}
