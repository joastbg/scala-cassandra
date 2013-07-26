package net.joastbg.testing

import scala.collection.JavaConversions._
import scala.reflect.BeanProperty

import java.util.{UUID, ArrayList}
import javax.persistence.{Table, Entity}

import me.prettyprint.hom._
import me.prettyprint.hom.annotations._

import me.prettyprint.hector.api.Keyspace

// Using Hector-Object-Mapper

trait CassandraModel[K <: Object] {
    implicit def entityManager(implicit ks: Keyspace) = new EntityManagerImpl(ks, "net.joastbg.testing")
    def save()(implicit em: EntityManagerImpl) { em.persist(this) }
}

trait CassandraMeta[K <: Object, V <: CassandraModel[K]] {
    def freshInstance: V
    def find[V](k: K)(implicit em: EntityManagerImpl) = Option(em.find(freshInstance.getClass, k))
}

/// Address

@Entity
@Table(name="Address")
class Address extends CassandraModel[UUID] {

    @Id
    @BeanProperty
    var id: UUID = UUID.randomUUID()

    @Column(name="streetAddress")
    @BeanProperty
    var streetAddress: String = _

    @Column(name="city")
    @BeanProperty
    var city: String = _

    @Column(name="state")
    @BeanProperty
    var state: String = _

    @Column(name="postalCode")
    @BeanProperty
    var postalCode: String = _
}

object Address extends Address with CassandraMeta[UUID, Address] {
    def freshInstance = new Address
}

/// PhoneNumber

@Entity
@Table(name="PhoneNumber")
class PhoneNumber extends CassandraModel[UUID] {

    @Id
    @BeanProperty
    var id: UUID = UUID.randomUUID()

    @Column(name="kind")
    @BeanProperty
    var kind: String = _

    @Column(name="number")
    @BeanProperty
    var number: String = _
}

object PhoneNumber extends PhoneNumber with CassandraMeta[UUID, PhoneNumber] {
    def freshInstance = new PhoneNumber
}

/// Person

@Entity
@Table(name="Person")
class Person extends CassandraModel[UUID] {

    @Id
    @BeanProperty
    var id: UUID = UUID.randomUUID()
 
    @Column(name="firstname")
    @BeanProperty
    var firstName: String = _

    @Column(name="lastname")
    @BeanProperty
    var lastName: String = _
 
    @Column(name="Address")
    @BeanProperty
    var address: UUID = UUID.randomUUID()
 
    def getAddress(implicit ks: Keyspace) = Address.find(address)

    @Column(name="PhoneNumber")
    @BeanProperty
    var phoneNumber: UUID = UUID.randomUUID()
 
    def getPhoneNumber(implicit ks: Keyspace) = PhoneNumber.find(phoneNumber)
}

object Person extends Person with CassandraMeta[UUID, Person] {
    def freshInstance = new Person
}