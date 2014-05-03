package net.joastbg.testing

import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.collection.JavaConverters.seqAsJavaListConverter

import scala.collection.immutable.HashMap
import scala.collection.JavaConversions._

import me.prettyprint.cassandra.serializers.LongSerializer
import me.prettyprint.cassandra.serializers.StringSerializer
import me.prettyprint.cassandra.service.template.ColumnFamilyResult
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate
import me.prettyprint.cassandra.service.ThriftKsDef
import me.prettyprint.hector.api.beans.OrderedRows
import me.prettyprint.hector.api.beans.Row
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition
import me.prettyprint.hector.api.ddl.ComparatorType
import me.prettyprint.hector.api.ddl.KeyspaceDefinition
import me.prettyprint.hector.api.factory.HFactory
import me.prettyprint.hector.api.query.QueryResult
import me.prettyprint.hector.api.Cluster
import me.prettyprint.hector.api.Keyspace

import me.prettyprint.hom.EntityManagerImpl

/**
 * Cassandra wrapper using Hector
 * Works with scala 2.10.0
 *
 * @author Johan Astborg
 */
class CassandraWrapper {

	private val clusterName = "Test Cluster"
	private val hostIp = "localhost:9160"
	private val keyspaceName = "raptortesting"
	private val columnFamilyName = "tenants"

	private val cluster: Cluster = HFactory.getOrCreateCluster(clusterName, hostIp)
	private val keyspace: Keyspace = HFactory.createKeyspace(keyspaceName, cluster)
	private val template = new ThriftColumnFamilyTemplate[String, String](keyspace, columnFamilyName, StringSerializer.get(), StringSerializer.get())

	private implicit def entityManager(implicit ks: Keyspace) = new EntityManagerImpl(ks, "net.joastbg.testing")

	// Makes extraction of optional fields easier
	implicit def enrichValueExtraction(value: String) = new AnyRef {
		def textOption : Option[String] = {
			if (value == null || value.length == 0) None else Some(value)
		}
	}

	/**
	 * Saves column/value for a key
	 */
	def saveKV(key: String, pair: Tuple2[String, String]) {
		val updater: ColumnFamilyUpdater[String, String] = template.createUpdater(key)
		updater.setString(pair._1, pair._2)		
		template.update(updater)
	}

	/**
	 * Load value for key/column
	 */
	def loadKV(key: String, column: String) : Tuple2[String, String] = {
		val res: ColumnFamilyResult[String, String] = template.queryColumns(key)
		Tuple2(column, res.getString(column))
	}

	/**
	 * Saves a map with (key, value) pairs to database
	 * Note: Will override if exists for key
	 */
	def saveKVs(key: String, map: Map[String, String]) {
		val updater: ColumnFamilyUpdater[String, String] = template.createUpdater(key)
		map.foreach(kv => println(updater.setString(kv._1, kv._2)))
		template.update(updater);
	}

	/**
	 * Loads a map of (key, value) pairs
	 * Returns empty map if key not present
	 */
	def loadKVs(key: String) : Map[String, String] = key match {
		case key if key != null && key.nonEmpty =>
			val res: ColumnFamilyResult[String, String] = template.queryColumns(key)
			res.getColumnNames.map(f => f -> res.getString(f)).toMap
		case _ => Map()
	}

	def runDemo() {
		
		implicit def entityManager(implicit ks: Keyspace) = new EntityManagerImpl(ks, "net.joastbg.testing")
		implicit def em: EntityManagerImpl = entityManager(keyspace)

		def cfDef: ColumnFamilyDefinition = HFactory.createColumnFamilyDefinition(keyspaceName, "PhoneNumber");
    	cluster.addColumnFamily(cfDef);

		//val phoneNr = new PhoneNumber
		//phoneNr.kind = "home"
		//phoneNr.number = "212 555-1234"

		//phoneNr.save()

		PhoneNumber.find(phoneNr.id) match {
			case Some(nr) => println("Found number: " + nr.number)
			case _ => println("Nothing found")
		}
	}
}

object TestApplication extends CassandraWrapper {

	def main(args: Array[String]) {

		// TODO: Make this nicer
		//implicit def entityManager(implicit ks: Keyspace) = new EntityManagerImpl(ks, "net.joastbg.testing")
		//implicit def em: EntityManagerImpl = entityManager(keyspace)

		// TODO: Create keyspace, create column-family

		TestApplication.runDemo();
		
	}
}