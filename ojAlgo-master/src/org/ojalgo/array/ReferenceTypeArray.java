/*
 * Copyright 1997-2017 Optimatika
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.array;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;

import org.ojalgo.access.Access1D;
import org.ojalgo.access.Mutate1D;
import org.ojalgo.constant.PrimitiveMath;
import org.ojalgo.function.BinaryFunction;
import org.ojalgo.function.FunctionUtils;
import org.ojalgo.function.NullaryFunction;
import org.ojalgo.function.ParameterFunction;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.function.VoidFunction;

/**
 * A one- and/or arbitrary-dimensional array of {@linkplain java.lang.Number}.
 *
 * @author apete
 */
abstract class ReferenceTypeArray<N extends Number> extends PlainArray<N> implements Mutate1D.Sortable {

    public static <N extends Number> void exchange(final N[] data, final int firstA, final int firstB, final int step, final int aCount) {

        int tmpIndexA = firstA;
        int tmpIndexB = firstB;

        N tmpVal;

        for (int i = 0; i < aCount; i++) {

            tmpVal = data[tmpIndexA];
            data[tmpIndexA] = data[tmpIndexB];
            data[tmpIndexB] = tmpVal;

            tmpIndexA += step;
            tmpIndexB += step;
        }
    }

    public static <N extends Number> void fill(final N[] data, final int first, final int limit, final int step, final N value) {
        for (int i = first; i < limit; i += step) {
            data[i] = value;
        }
    }

    public static <N extends Number> void fill(final N[] data, final int first, final int limit, final int step, final NullaryFunction<N> supplier) {
        for (int i = first; i < limit; i += step) {
            data[i] = supplier.invoke();
        }
    }

    public static <N extends Number> void invoke(final N[] data, final int first, final int limit, final int step, final Access1D<N> left,
            final BinaryFunction<N> function, final Access1D<N> right) {
        for (int i = first; i < limit; i += step) {
            data[i] = function.invoke(left.get(i), right.get(i));
        }
    }

    public static <N extends Number> void invoke(final N[] data, final int first, final int limit, final int step, final Access1D<N> left,
            final BinaryFunction<N> function, final N right) {
        for (int i = first; i < limit; i += step) {
            data[i] = function.invoke(left.get(i), right);
        }
    }

    public static <N extends Number> void invoke(final N[] data, final int first, final int limit, final int step, final Access1D<N> value,
            final ParameterFunction<N> function, final int aParam) {
        for (int i = first; i < limit; i += step) {
            data[i] = function.invoke(value.get(i), aParam);
        }
    }

    public static <N extends Number> void invoke(final N[] data, final int first, final int limit, final int step, final Access1D<N> value,
            final UnaryFunction<N> function) {
        for (int i = first; i < limit; i += step) {
            data[i] = function.invoke(value.get(i));
        }
    }

    public static <N extends Number> void invoke(final N[] data, final int first, final int limit, final int step, final N left,
            final BinaryFunction<N> function, final Access1D<N> right) {
        for (int i = first; i < limit; i += step) {
            data[i] = function.invoke(left, right.get(i));
        }
    }

    public static <N extends Number> void invoke(final N[] data, final int first, final int limit, final int step, final VoidFunction<N> aVisitor) {
        for (int i = first; i < limit; i += step) {
            aVisitor.invoke(data[i]);
        }
    }

    public final N[] data;

    ReferenceTypeArray(final N[] data) {

        super(data.length);

        this.data = data;
    }

    @Override
    public boolean equals(final Object anObj) {
        if (anObj instanceof ReferenceTypeArray) {
            return Arrays.equals(data, ((ReferenceTypeArray<?>) anObj).data);
        } else {
            return super.equals(anObj);
        }
    }

    public final void fillMatching(final Access1D<N> left, final BinaryFunction<N> function, final Access1D<N> right) {
        final int tmpLimit = (int) FunctionUtils.min(this.count(), left.count(), right.count());
        for (int i = 0; i < tmpLimit; i++) {
            data[i] = function.invoke(left.get(i), right.get(i));
        }
    }

    public final void fillMatching(final UnaryFunction<N> function, final Access1D<N> arguments) {
        final int tmpLimit = (int) FunctionUtils.min(this.count(), arguments.count());
        for (int i = 0; i < tmpLimit; i++) {
            data[i] = function.invoke(arguments.get(i));
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public final void reset() {
        Arrays.fill(data, this.valueOf(PrimitiveMath.ZERO));
    }

    public final Spliterator<N> spliterator() {
        return Spliterators.spliterator(data, 0, data.length, PlainArray.CHARACTERISTICS);
    }

    public final N[] copyOfData() {
        return Raw1D.copyOf(data);
    }

    @Override
    public final double doubleValue(final int index) {
        return data[index].doubleValue();
    }

    @Override
    public final void exchange(final int firstA, final int firstB, final int step, final int count) {
        ReferenceTypeArray.exchange(data, firstA, firstB, step, count);
    }

    @Override
    public final void fill(final int first, final int limit, final Access1D<N> left, final BinaryFunction<N> function, final Access1D<N> right) {
        ReferenceTypeArray.invoke(data, first, limit, 1, left, function, right);
    }

    @Override
    public final void fill(final int first, final int limit, final Access1D<N> left, final BinaryFunction<N> function, final N right) {
        ReferenceTypeArray.invoke(data, first, limit, 1, left, function, right);
    }

    @Override
    public final void fill(final int first, final int limit, final int step, final N value) {
        ReferenceTypeArray.fill(data, first, limit, step, value);
    }

    @Override
    public final void fill(final int first, final int limit, final int step, final NullaryFunction<N> supplier) {
        ReferenceTypeArray.fill(data, first, limit, step, supplier);
    }

    @Override
    public final void fill(final int first, final int limit, final N left, final BinaryFunction<N> function, final Access1D<N> right) {
        ReferenceTypeArray.invoke(data, first, limit, 1, left, function, right);
    }

    @Override
    public final void fillOne(final int index, final Access1D<?> values, final long valueIndex) {
        data[index] = this.valueOf(values.get(valueIndex));
    }

    @Override
    public final void fillOne(final int index, final N value) {
        data[index] = value;

    }

    @Override
    public final void fillOne(final int index, final NullaryFunction<N> supplier) {
        data[index] = supplier.get();
    }

    @Override
    public final N get(final int index) {
        return data[index];
    }

    @Override
    public final void modify(final int first, final int limit, final int step, final Access1D<N> left, final BinaryFunction<N> function) {
        ReferenceTypeArray.invoke(data, first, limit, step, left, function, this);
    }

    @Override
    public final void modify(final int first, final int limit, final int step, final BinaryFunction<N> function, final Access1D<N> right) {
        ReferenceTypeArray.invoke(data, first, limit, step, this, function, right);
    }

    @Override
    public final void modify(final int first, final int limit, final int step, final BinaryFunction<N> function, final N right) {
        ReferenceTypeArray.invoke(data, first, limit, step, this, function, right);
    }

    @Override
    public final void modify(final int first, final int limit, final int step, final N left, final BinaryFunction<N> function) {
        ReferenceTypeArray.invoke(data, first, limit, step, left, function, this);
    }

    @Override
    public final void modify(final int first, final int limit, final int step, final ParameterFunction<N> function, final int parameter) {
        ReferenceTypeArray.invoke(data, first, limit, step, this, function, parameter);
    }

    @Override
    public final void modify(final int first, final int limit, final int step, final UnaryFunction<N> function) {
        ReferenceTypeArray.invoke(data, first, limit, step, this, function);
    }

    @Override
    public final void modifyOne(final int index, final UnaryFunction<N> modifier) {
        data[index] = modifier.invoke(data[index]);
    }

    @Override
    public final int searchAscending(final N value) {
        return Arrays.binarySearch(data, value);
    }

    @Override
    public final void set(final int index, final double value) {
        data[index] = this.valueOf(value);
    }

    @Override
    public final void set(final int index, final Number value) {
        data[index] = this.valueOf(value);
    }

    @Override
    public final int size() {
        return data.length;
    }

    @Override
    public final void visit(final int first, final int limit, final int step, final VoidFunction<N> visitor) {
        ReferenceTypeArray.invoke(data, first, limit, step, visitor);
    }

    @Override
    public void visitOne(final int index, final VoidFunction<N> visitor) {
        visitor.invoke(data[index]);
    }

    @Override
    final boolean isPrimitive() {
        return false;
    }

    @Override
    final void modify(final long extIndex, final int intIndex, final Access1D<N> left, final BinaryFunction<N> function) {
        data[intIndex] = function.invoke(left.get(extIndex), data[intIndex]);
    }

    @Override
    final void modify(final long extIndex, final int intIndex, final BinaryFunction<N> function, final Access1D<N> right) {
        data[intIndex] = function.invoke(data[intIndex], right.get(extIndex));
    }

    @Override
    final void modify(final long extIndex, final int intIndex, final UnaryFunction<N> function) {
        data[intIndex] = function.invoke(data[intIndex]);
    }

    abstract N valueOf(double value);

    abstract N valueOf(Number number);

}
