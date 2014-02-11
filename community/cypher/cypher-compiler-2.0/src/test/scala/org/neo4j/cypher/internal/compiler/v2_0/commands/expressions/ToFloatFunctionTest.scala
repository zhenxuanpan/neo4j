/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v2_0.commands.expressions

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FunSuite}
import org.neo4j.cypher.internal.compiler.v2_0.ExecutionContext
import org.neo4j.cypher.internal.compiler.v2_0.pipes.QueryStateHelper

@RunWith(classOf[JUnitRunner])
class ToFloatFunctionTest extends FunSuite with Matchers {

  test("should return null if argument is null") {
    toFloat(null.asInstanceOf[Any]) should be(null.asInstanceOf[Int])
  }

  test("should convert a string to a float") {
    toFloat("10.599") should be(10.599)
  }

  test("should convert an integer string to a float") {
    toFloat("21") should be(21.0)
  }

  test("should convert an integer to a float") {
    toFloat(23) should be(23.0)
  }

  test("should throw an exception if the argument is a non-numeric string") {
    evaluating { toFloat("20foobar2") } should produce[IllegalArgumentException]
  }

  test("should throw an exception if the argument is a hexadecimal string") {
    evaluating { toFloat("0x20") } should produce[IllegalArgumentException]
  }

  test("should convert a string with leading zeros to a float") {
    toFloat("000123121.5") should be(123121.5)
  }

  test("should convert a string with leading minus to a negative float") {
    toFloat("-12.66") should be(-12.66)
  }

  test("should convert a string with leading minus and zeros to a negative float") {
    toFloat("-00012.91") should be(-12.91)
  }

  test("should throw an exception if the argument is an object which cannot be converted to a float") {
    evaluating { toFloat(new Object) } should produce[IllegalArgumentException]
  }

  test("given a float should give the same value back") {
    toFloat(50.5) should be(50.5)
  }

  test("given a boolean should return the numeric value") {
    toFloat(false) should be(0.0)
    toFloat(true) should be(1.0)
  }

  test("should throw an exception if the argument is a boolean string literal") {
    evaluating { toFloat("false") } should produce[IllegalArgumentException]
    evaluating { toFloat("true") } should produce[IllegalArgumentException]
  }

  private def toFloat(orig: Any) = {
    ToFloatFunction(Literal(orig))(ExecutionContext.empty)(QueryStateHelper.empty)
  }
}
