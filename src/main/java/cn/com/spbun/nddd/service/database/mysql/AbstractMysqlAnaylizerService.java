package cn.com.spbun.nddd.service.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cn.com.spbun.nddd.gui.console.ConsoleDialog;
import cn.com.spbun.nddd.model.ConnectionInfo;
import cn.com.spbun.nddd.model.DBConfigItem;

/**
 * 抽象类
 * 
 * @author NOLY DAKE
 *
 */
public abstract class AbstractMysqlAnaylizerService implements MysqlAnaylizerServiceI {

    /** 日志对象 */
    private static final Logger LOG = Logger.getLogger(AbstractMysqlAnaylizerService.class);

    protected ConsoleDialog console = null;

    @Override
    public void setConsole(ConsoleDialog console) {
        this.console = console;
    }

    protected void console(String message) {
        if (console != null) {
            console.console(message);
        }
    }

    // mysql内存计算公式 
    // mysql used mem =key_buffer_size + query_cache_size + tmp_table_size + innodb_buffer_pool_size + innodb_additional_mem_pool_size + innodb_log_buffer_size + max_connections * ( 
    // read_buffer_size + read_rnd_buffer_size + sort_buffer_size+ join_buffer_size + binlog_cache_size + thread_stack )

    public static Map<String, String> VARIABLES_DESC = new HashMap<String, String>();
    public static Map<String, String> COLUMNTYPE_JAVATYPE = new HashMap<String, String>();

    static {
        VARIABLES_DESC.put("back_log",
                "指定MySQL可能的连接数量。当MySQL主线程在很短的时间内得到非常多的连接请求，该参数就起作用，之后主线程花些时间(尽管很短)检查连接并且启动一个新线程。\r\nback_log参数的值指出在MySQL暂时停止响应新请求之前的短时间内多少个请求可以被存在堆栈中。如果系统在一个短时间内有很多连接，则需要增大该参数的值，该参数值指定到来的TCP/IP连接的侦听队列的大小。不同的操作系统在这个队列大小上有它自己的限制。 试图设定back_log高于你的操作系统的限制将是无效的。\r\n当观察MySQL进程列表，发现大量 264084 | unauthenticated user | xxx.xxx.xxx.xxx | NULL | Connect | NULL | login | NULL 的待连接进程时，就要加大 back_log 的值。back_log默认值为50。");
        VARIABLES_DESC.put("basedir", "MySQL主程序所在路径，即：--basedir参数的值。");
        VARIABLES_DESC.put("bdb_cache_size",
                "分配给BDB类型数据表的缓存索引和行排列的缓冲区大小，如果不使用DBD类型数据表，则应该在启动MySQL时加载 --skip-bdb 参数以避免内存浪费。");
        VARIABLES_DESC.put("bdb_log_buffer_size",
                "分配给BDB类型数据表的缓存索引和行排列的缓冲区大小，如果不使用DBD类型数据表，则应该将该参数值设置为0，或者在启动MySQL时加载 --skip-bdb 参数以避免内存浪费。");
        VARIABLES_DESC.put("bdb_home", "参见 --bdb-home 选项。");
        VARIABLES_DESC.put("bdb_max_lock",
                "指定最大的锁表进程数量(默认为10000)，如果使用BDB类型数据表，则可以使用该参数。如果在执行大型事物处理或者查询时发现 bdb: Lock table is out of available locks or Got error 12 from ... 错误，则应该加大该参数值。");
        VARIABLES_DESC.put("bdb_logdir", "指定使用BDB类型数据表提供服务时的日志存放位置。即为 --bdb-logdir 的值。");
        VARIABLES_DESC.put("bdb_shared_data", "如果使用 --bdb-shared-data 选项则该参数值为On。");
        VARIABLES_DESC.put("bdb_tmpdir", "BDB类型数据表的临时文件目录。即为 --bdb-tmpdir 的值。");
        VARIABLES_DESC.put("binlog_cache_size",
                "为binary log指定在查询请求处理过程中SQL 查询语句使用的缓存大小。如果频繁应用于大量、复杂的SQL表达式处理，则应该加大该参数值以获得性能提升。");
        VARIABLES_DESC.put("bulk_insert_buffer_size",
                "指定 MyISAM 类型数据表表使用特殊的树形结构的缓存。使用整块方式(bulk)能够加快插入操作( INSERT ... SELECT, INSERT ... VALUES (...), (...), ..., 和 LOAD DATA INFILE) 的速度和效率。该参数限制每个线程使用的树形结构缓存大小，如果设置为0则禁用该加速缓存功能。注意：该参数对应的缓存操作只能用户向非空数据表中执行插入操作!默认值为 8MB。");
        VARIABLES_DESC.put("character_set", "MySQL的默认字符集。");
        VARIABLES_DESC.put("character_sets", "MySQL所能提供支持的字符集。");
        VARIABLES_DESC.put("concurrent_inserts",
                "如果开启该参数，MySQL则允许在执行 SELECT 操作的同时进行 INSERT 操作。如果要关闭该参数，可以在启动 mysqld 时加载 --safe 选项，或者使用 --skip-new 选项。默认为On。");
        VARIABLES_DESC.put("connect_timeout", "指定MySQL服务等待应答一个连接报文的最大秒数，超出该时间，MySQL向客户端返回 bad handshake。");
        VARIABLES_DESC.put("datadir", "指定数据库路径。即为 --datadir 选项的值。");
        VARIABLES_DESC.put("auto_increment_offset", "表示自增长字段从那个数开始，他的取值范围是1 .. 65535");
        VARIABLES_DESC.put("auto_increment_increment", "表示自增长字段每次递增的量，其默认值是1，取值范围是1 .. 65535");
        VARIABLES_DESC.put("delay_key_write",
                "该参数只对 MyISAM 类型数据表有效。有如下的取值种类：off: 如果在建表语句中使用 CREATE TABLE ... DELAYED_KEY_WRITES，则全部忽略DELAYED_KEY_WRITES;on: 如果在建表语句中使用 CREATE TABLE ... DELAYED_KEY_WRITES，则使用该选项(默认);all: 所有打开的数据表都将按照 DELAYED_KEY_WRITES 处理。如果 DELAYED_KEY_WRITES 开启，对于已经打开的数据表而言，在每次索引更新时都不刷新带有DELAYED_KEY_WRITES 选项的数据表的key buffer，除非该数据表关闭。该参数会大幅提升写入键值的速度。如果使用该参数，则应该检查所有数据表：myisamchk --fast --force。");
        VARIABLES_DESC.put("delayed_insert_limit",
                "在插入delayed_insert_limit行后，INSERT DELAYED处理模块将检查是否有未执行的SELECT语句。如果有，在继续处理前执行允许这些语句。");
        VARIABLES_DESC.put("delayed_insert_timeout", "一个INSERT DELAYED线程应该在终止之前等待INSERT语句的时间。");
        VARIABLES_DESC.put("delayed_queue_size",
                "为处理INSERT DELAYED分配的队列大小(以行为单位)。如果排队满了，任何进行INSERT DELAYED的客户必须等待队列空间释放后才能继续。");
        VARIABLES_DESC.put("flush", "在启动MySQL时加载 --flush 参数打开该功能。");
        VARIABLES_DESC.put("flush_time",
                "如果该设置为非0值，那么每flush_time秒，所有打开的表将被关，以释放资源和sync到磁盘。注意：只建议在使用 Windows9x/Me 或者当前操作系统资源严重不足时才使用该参数!");
        VARIABLES_DESC.put("ft_boolean_syntax", "搜索引擎维护员希望更改允许用于逻辑全文搜索的操作符。这些则由变量 ft_boolean_syntax 控制。");
        VARIABLES_DESC.put("ft_min_word_len", "指定被索引的关键词的最小长度。注意：在更改该参数值后，索引必须重建!");
        VARIABLES_DESC.put("ft_max_word_len", "指定被索引的关键词的最大长度。注意：在更改该参数值后，索引必须重建!");
        VARIABLES_DESC.put("ft_max_word_len_for_sort",
                "指定在使用REPAIR, CREATE INDEX, or ALTER TABLE等方法进行快速全文索引重建过程中所能使用的关键词的最大长度。超出该长度限制的关键词将使用低速方式进行插入。加大该参数的值，MySQL将会建立更大的临时文件(这会减轻CPU负载，但效率将取决于磁盘I/O效率)，并且在一个排序取内存放更少的键值。");
        VARIABLES_DESC.put("ft_stopword_file", "从 ft_stopword_file 变量指定的文件中读取列表。在修改了 stopword 列表后，必须重建 FULLTEXT 索引。");
        VARIABLES_DESC.put("have_innodb", "YES: MySQL支持InnoDB类型数据表; DISABLE: 使用 --skip-innodb 关闭对InnoDB类型数据表的支持。");
        VARIABLES_DESC.put("have_bdb", "YES: MySQL支持伯克利类型数据表; DISABLE: 使用 --skip-bdb 关闭对伯克利类型数据表的支持。");
        VARIABLES_DESC.put("have_raid", "YES: 使MySQL支持RAID功能。");
        VARIABLES_DESC.put("have_openssl", "YES: 使MySQL支持SSL加密协议。");
        VARIABLES_DESC.put("init_file", "指定一个包含SQL查询语句的文件，该文件在MySQL启动时将被加载，文件中的SQL语句也会被执行。");
        VARIABLES_DESC.put("interactive_timeout", "服务器关闭交互式连接前等待活动的描述。默认值为28800(8小时)服务器在关上它前在一个交互连接上等待行动的秒数。"
                + "一个交互的客户被定义为对mysql_real_connect()使用CLIENT_INTERACTIVE选项的客户。也可见wait_timeout。");
        VARIABLES_DESC.put("join_buffer_size",
                "用于全部联合(join)的缓冲区大小(不是用索引的联结)。缓冲区对2个表间的每个全部联结分配一次缓冲区，当增加索引不可能时，增加该值可得到一个更快的全部联结。(通常得到快速联结的最佳方法是增加索引。)");
        VARIABLES_DESC.put("key_buffer_size",
                "用于索引块的缓冲区大小，增加它可得到更好处理的索引(对所有读和多重写)，到你能负担得起那样多。如果你使它太大，系统将开始变慢慢。必须为OS文件系统缓存留下一些空间。为了在写入多个行时得到更多的速度。");

        VARIABLES_DESC.put("language", "用户输出报错信息的语言。");
        VARIABLES_DESC.put("large_file_support", "开启大文件支持。");
        VARIABLES_DESC.put("locked_in_memory", "使用 --memlock 将mysqld锁定在内存中。");
        VARIABLES_DESC.put("log", "记录所有查询操作。");
        VARIABLES_DESC.put("log_update", "开启update log。");
        VARIABLES_DESC.put("log_bin", "开启 binary log");
        VARIABLES_DESC.put("log_slave_updates", "如果使用链状同步或者多台Slave之间进行同步则需要开启此参数。");
        VARIABLES_DESC.put("long_query_time", "如果一个查询所用时间超过该参数值，则该查询操作将被记录在Slow_queries中。");
        VARIABLES_DESC.put("lower_case_table_names",
                "1: MySQL总使用小写字母进行SQL操作;0: 关闭该功能。注意：如果使用该参数，则应该在启用前将所有数据表转换为小写字母。");
        VARIABLES_DESC.put("max_allowed_packet",
                "一个查询语句包的最大尺寸。消息缓冲区被初始化为net_buffer_length字节，但是可在需要时增加到max_allowed_packet个字节。该值太小则会在处理大包时产生错误。如果使用大的BLOB列，必须增加该值。");
        VARIABLES_DESC.put("net_buffer_length",
                "通信缓冲区在查询期间被重置到该大小。通常不要改变该参数值，但是如果内存不足，可以将它设置为查询期望的大小。(即，客户发出的SQL语句期望的长度。如果语句超过这个长度，缓冲区自动地被扩大，直到max_allowed_packet个字节。)");
        VARIABLES_DESC.put("max_binlog_cache_size", "指定binary log缓存的最大容量，如果设置的过小，则在执行复杂查询语句时MySQL会出错。");
        VARIABLES_DESC.put("max_binlog_size", "指定binary log文件的最大容量，默认为1GB。");
        VARIABLES_DESC.put("max_connections",
                "允许同时连接MySQL服务器的客户数量。如果超出该值，MySQL会返回Too many connections错误，但通常情况下，MySQL能够自行解决。该值的范围为100-16384，默认值为100");
        VARIABLES_DESC.put("max_connect_errors", "对于同一主机，如果有超出该参数值个数的中断错误连接，则该主机将被禁止连接。如需对该主机进行解禁，执行：FLUSH HOST;。");
        VARIABLES_DESC.put("max_delayed_threads",
                "不要启动多于的这个数字的线程来处理INSERT DELAYED语句。如果你试图在所有INSERT DELAYED线程在用后向一张新表插入数据，行将被插入，就像DELAYED属性没被指定那样。");
        VARIABLES_DESC.put("max_heap_table_size", "内存表所能使用的最大容量。");
        VARIABLES_DESC.put("max_join_size",
                "如果要查询多于max_join_size个记录的联合将返回一个错误。如果要执行没有一个WHERE的语句并且耗费大量时间，且返回上百万行的联结，则需要加大该参数值。");
        VARIABLES_DESC.put("max_sort_length", "在排序BLOB或TEXT值时使用的字节数(每个值仅头max_sort_length个字节被使用;其余的被忽略)。");
        VARIABLES_DESC.put("max_user_connections", "指定来自同一用户的最多连接数。设置为0则代表不限制。");
        VARIABLES_DESC.put("max_tmp_tables", "(该参数目前还没有作用)。一个客户能同时保持打开的临时表的最大数量。");
        VARIABLES_DESC.put("max_write_lock_count",
                "当出现max_write_lock_count个写入锁定数量后，开始允许一些被锁定的读操作开始执行。避免写入锁定过多，读取操作处于长时间等待状态。");
        VARIABLES_DESC.put("wait_timeout", "服务器关闭非交互式连接前等待活动的描述。默认值为28800(8小时)");

        COLUMNTYPE_JAVATYPE.put("varchar", "String");
        COLUMNTYPE_JAVATYPE.put("char", "String");
        COLUMNTYPE_JAVATYPE.put("tinytext", "String");
        COLUMNTYPE_JAVATYPE.put("text", "String");
        COLUMNTYPE_JAVATYPE.put("enum", "String");
        COLUMNTYPE_JAVATYPE.put("longtext", "String");
        COLUMNTYPE_JAVATYPE.put("mediumtext", "String");
        COLUMNTYPE_JAVATYPE.put("set", "String");
        COLUMNTYPE_JAVATYPE.put("blob", "byte[]");
        COLUMNTYPE_JAVATYPE.put("bit", "byte[]");
        COLUMNTYPE_JAVATYPE.put("longblob", "byte[]");
        COLUMNTYPE_JAVATYPE.put("mediumblob", "byte[]");
        COLUMNTYPE_JAVATYPE.put("integer", "Integer");
        COLUMNTYPE_JAVATYPE.put("int", "Integer");
        COLUMNTYPE_JAVATYPE.put("smallint", "Integer");
        COLUMNTYPE_JAVATYPE.put("mediumint", "Integer");
        COLUMNTYPE_JAVATYPE.put("bigint", "Long");
        COLUMNTYPE_JAVATYPE.put("date", "Date");
        COLUMNTYPE_JAVATYPE.put("year", "Date");
        COLUMNTYPE_JAVATYPE.put("datetime", "Date");
        COLUMNTYPE_JAVATYPE.put("timestamp", "Timestamp");
        COLUMNTYPE_JAVATYPE.put("decimal", "BigDecimal");
        COLUMNTYPE_JAVATYPE.put("double", "Double");
        COLUMNTYPE_JAVATYPE.put("float", "Float");
        COLUMNTYPE_JAVATYPE.put("time", "Time");
        COLUMNTYPE_JAVATYPE.put("tinyint", "Boolean");
        COLUMNTYPE_JAVATYPE.put("tinyblob", "byte[]");

    }

    @Override
    public List<DBConfigItem> getVariables(ConnectionInfo info) {

        List<DBConfigItem> keyValues = new ArrayList<DBConfigItem>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(info);

            String sql = "SHOW VARIABLES";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                keyValues.add(new DBConfigItem(rs.getString(1), rs.getString(2)));
            }

        } catch (Exception es) {

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

        return keyValues;
    }

    protected Connection getConnection(ConnectionInfo info) throws ClassNotFoundException, SQLException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("获取数据库连接开始...");
        }

        try {
            Class.forName(info.getJdbcDriverName());

            Connection result = DriverManager.getConnection(info.getJdbcConnString(), info.getUsername(),
                    info.getPassword());

            if (LOG.isDebugEnabled()) {
                LOG.debug("获取数据库连接结束");
            }

            return result;

        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw e;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw e;
        }
    }
}
