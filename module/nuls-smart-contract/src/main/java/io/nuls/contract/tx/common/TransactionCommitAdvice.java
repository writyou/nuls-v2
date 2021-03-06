/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2019 nuls.io
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
package io.nuls.contract.tx.common;

import io.nuls.base.data.BlockHeader;
import io.nuls.base.data.Transaction;
import io.nuls.base.protocol.CommonAdvice;
import io.nuls.base.protocol.ProtocolGroupManager;
import io.nuls.contract.config.ContractContext;
import io.nuls.contract.enums.BlockType;
import io.nuls.contract.helper.ContractHelper;
import io.nuls.contract.manager.ChainManager;
import io.nuls.contract.model.bo.Chain;
import io.nuls.contract.model.dto.ContractPackageDto;
import io.nuls.contract.model.po.ContractOfflineTxHashPo;
import io.nuls.contract.storage.ContractOfflineTxHashListStorageService;
import io.nuls.contract.tx.v1.CallContractProcessor;
import io.nuls.contract.util.Log;
import io.nuls.core.constant.TxType;
import io.nuls.core.core.annotation.Autowired;
import io.nuls.core.core.annotation.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: PierreLuo
 * @date: 2019-05-27
 */
@Component
public class TransactionCommitAdvice implements CommonAdvice {

    @Autowired
    private ContractHelper contractHelper;
    @Autowired
    private ContractOfflineTxHashListStorageService contractOfflineTxHashListStorageService;
    @Autowired
    private CallContractProcessor callContractProcessor;

    @Override
    public void begin(int chainId, List<Transaction> txList, BlockHeader header) {
        try {
            ChainManager.chainHandle(chainId, BlockType.VERIFY_BLOCK.type());
            ContractPackageDto contractPackageDto = contractHelper.getChain(chainId).getBatchInfo().getContractPackageDto();
            if (contractPackageDto != null) {
                Log.info("contract execute txDataSize is {}, commit txDataSize is {}", contractPackageDto.getContractResultMap().keySet().size(), txList.size());

                List<byte[]> offlineTxHashList = contractPackageDto.getOfflineTxHashList();
                if(offlineTxHashList != null && !offlineTxHashList.isEmpty()) {
                    // 保存智能合约链下交易hash
                    contractOfflineTxHashListStorageService.saveOfflineTxHashList(chainId, header.getHash().getBytes(), new ContractOfflineTxHashPo(contractPackageDto.getOfflineTxHashList()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void end(int chainId, List<Transaction> txList, BlockHeader blockHeader) {
        // 移除临时余额, 临时区块头等当前批次执行数据
        Chain chain = contractHelper.getChain(chainId);
        chain.setBatchInfo(null);
    }
}
