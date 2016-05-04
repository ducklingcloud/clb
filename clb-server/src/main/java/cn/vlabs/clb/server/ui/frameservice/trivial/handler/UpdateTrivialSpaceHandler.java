/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.clb.server.ui.frameservice.trivial.handler;

import java.io.File;

import cn.vlabs.clb.api.trivial.TrivialParam;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.model.TrivialItem;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractAction;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.trivial.TrivialFacade;
import cn.vlabs.rest.ServiceException;

import com.mongodb.gridfs.GridFSDBFile;

public class UpdateTrivialSpaceHandler extends CLBAbstractAction {

    private TrivialFacade tf = AppFacade.getTrivialFacade();
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg) throws ServiceException, BaseException {
        TrivialParam tp = (TrivialParam) arg;
        try {
            int appid = af.getAppId();
            DPair p = df.convertToPair(appid,tp.getDocPair());
            DocMeta meta = df.getDocMetaByDocid(p.getAppid(),p.getDocid());
            af.checkPermission(meta);
            tf.removeTrivialFiles(tp.getSpaceName());
            DocVersion newDocVersion = df.getDocVersion(new DPair(appid, tp.getDocPair()));
            tf.checkFileOperation(newDocVersion);
            TrivialItem item = tf.getTrivialItemByStorageKey(tp.getSpaceName());
            GridFSDBFile newZipFile = df.readDocContent(newDocVersion);
            File tempNewZipFile = tf.writeZipFileToTempFile(newZipFile);
            tf.decompressZipFile(tempNewZipFile, item.getSpaceName(), tp.getUnzipBaseDir());
            int flag = tf.updateTrivialItemInfo(newDocVersion, item);
            return flag > 0;
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return null;
    }

}
