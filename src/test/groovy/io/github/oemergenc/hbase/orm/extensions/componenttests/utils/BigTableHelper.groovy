package io.github.oemergenc.hbase.orm.extensions.componenttests.utils

import com.google.cloud.bigtable.hbase.BigtableConfiguration
import com.google.cloud.bigtable.hbase.BigtableOptionsFactory
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder
import org.apache.hadoop.hbase.client.Connection
import org.apache.hadoop.hbase.client.TableDescriptorBuilder

class BigTableHelper {
    final int bigTablePort
    final String bigTableHost
    final String bigTableProjectId
    final String bigTableInstanceId
    final Configuration conf
    final Connection connection

    BigTableHelper(final int bigTablePort,
                   final String bigTableHost,
                   final String bigTableProjectId,
                   final String bigTableInstanceId) {
        this.bigTablePort = bigTablePort
        this.bigTableHost = bigTableHost
        this.bigTableProjectId = bigTableProjectId
        this.bigTableInstanceId = bigTableInstanceId

        conf = BigtableConfiguration.configure(bigTableProjectId, bigTableInstanceId)

        conf.set(BigtableOptionsFactory.BIGTABLE_ADMIN_HOST_KEY, bigTableHost + ":" + bigTablePort)
        conf.set(BigtableOptionsFactory.BIGTABLE_EMULATOR_HOST_KEY, bigTableHost + ":" + bigTablePort)
        conf.set(BigtableOptionsFactory.BIGTABLE_HOST_KEY, bigTableHost)
        conf.set(BigtableOptionsFactory.BIGTABLE_PORT_KEY, bigTablePort as String)
        conf.set(BigtableOptionsFactory.BIGTABLE_USE_PLAINTEXT_NEGOTIATION, "true")
        conf.set(BigtableOptionsFactory.PROJECT_ID_KEY, bigTableProjectId)
        conf.set(BigtableOptionsFactory.INSTANCE_ID_KEY, bigTableInstanceId)
        connection = BigtableConfiguration.connect(conf)
    }

    BigTableHelper(final String bigTableProjectId,
                   final String bigTableInstanceId) {
        this.bigTableProjectId = bigTableProjectId
        this.bigTableInstanceId = bigTableInstanceId

        conf = BigtableConfiguration.configure(bigTableProjectId, bigTableInstanceId)
        connection = BigtableConfiguration.connect(conf)
    }

    Connection connect() {
        return connection
    }

    def getTable(def tableName) {
        this.connection.getTable(TableName.valueOf(tableName))
    }

    def createTable(String name, List familyNames) {
        def families = familyNames.collect { ColumnFamilyDescriptorBuilder.of(it as String) }
        def tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(name))
                .setColumnFamilies(families)
                .build()
        connection.admin.createTable(tableDescriptor)
    }
}
