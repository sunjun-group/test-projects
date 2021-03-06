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
package org.ojalgo.matrix.decomposition;

/**
 * @author apete
 */
abstract class AbstractDecomposition<N extends Number> implements MatrixDecomposition<N> {

    private boolean myAspectRatioNormal = true;
    private boolean myComputed = false;
    private Boolean mySolvable = null;

    AbstractDecomposition() {
        super();
    }

    public final boolean isComputed() {
        return myComputed;
    }

    public final boolean isSolvable() {
        if (myComputed && (mySolvable == null)) {
            if (this instanceof MatrixDecomposition.Solver) {
                mySolvable = Boolean.valueOf(this.checkSolvability());
            } else {
                mySolvable = Boolean.FALSE;
            }
        }
        return myComputed && mySolvable.booleanValue();
    }

    public void reset() {
        myAspectRatioNormal = true;
        myComputed = false;
        mySolvable = null;
    }

    public abstract DecompositionStore<N> allocate(long numberOfRows, long numberOfColumns);

    public final boolean aspectRatioNormal(final boolean aspectRatioNormal) {
        return (myAspectRatioNormal = aspectRatioNormal);
    }

    public boolean checkSolvability() {
        return false;
    }

    public final boolean computed(final boolean computed) {
        return (myComputed = computed);
    }

    public abstract double getDimensionalEpsilon();

    public final boolean isAspectRatioNormal() {
        return myAspectRatioNormal;
    }

}
