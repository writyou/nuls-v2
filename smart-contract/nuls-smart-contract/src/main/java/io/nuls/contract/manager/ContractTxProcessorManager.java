/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2018 nuls.io
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.contract.manager;

import io.nuls.contract.model.bo.ContractWrapperTransaction;
import io.nuls.contract.processor.CallContractTxProcessor;
import io.nuls.contract.processor.CreateContractTxProcessor;
import io.nuls.contract.processor.DeleteContractTxProcessor;
import io.nuls.tools.basic.Result;
import io.nuls.tools.core.annotation.Autowired;
import io.nuls.tools.core.annotation.Component;

/**
 * @author: PierreLuo
 * @date: 2019-03-11
 */
@Component
public class ContractTxProcessorManager {

    @Autowired
    private CreateContractTxProcessor createContractTxProcessor;
    @Autowired
    private CallContractTxProcessor callContractTxProcessor;
    @Autowired
    private DeleteContractTxProcessor deleteContractTxProcessor;

    public Result createCommit(int chainId, ContractWrapperTransaction tx) {
        return createContractTxProcessor.onCommit(chainId, tx);
    }

    public Result createRollback(int chainId, ContractWrapperTransaction tx) throws Exception {
        return createContractTxProcessor.onRollback(chainId, tx);
    }

    public Result callCommit(int chainId, ContractWrapperTransaction tx) {
        return callContractTxProcessor.onCommit(chainId, tx);
    }

    public Result callRollback(int chainId, ContractWrapperTransaction tx) {
        return callContractTxProcessor.onRollback(chainId, tx);
    }

    public Result deleteCommit(int chainId, ContractWrapperTransaction tx) {
        return deleteContractTxProcessor.onCommit(chainId, tx);
    }

    public Result deleteRollback(int chainId, ContractWrapperTransaction tx) {
        return deleteContractTxProcessor.onRollback(chainId, tx);
    }

}
