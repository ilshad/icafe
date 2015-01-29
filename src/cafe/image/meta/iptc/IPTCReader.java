/**
 * Copyright (c) 2014 by Wen Yu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Any modifications to this file must keep this entire header intact.
 *
 * Change History - most recent changes go on top of previous changes
 *
 * IPTCReader.java
 *
 * Who   Date       Description
 * ====  =======    ============================================================
 * WY    12Jan2015  Initial creation to read IPTC information
 */

package cafe.image.meta.iptc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cafe.image.meta.MetadataReader;
import cafe.io.IOUtils;

/**
 * IPTC image metadata reader
 *  
 * @author Wen Yu, yuwen_66@yahoo.com
 * @version 1.0 01/12/2015
 */
public class IPTCReader implements MetadataReader {
	private Map<String, IPTCDataSet> datasetMap = new HashMap<String, IPTCDataSet>();
	private byte[] data;
	private boolean loaded;
	
	public IPTCReader(byte[] data) {
		this.data = data;
	}
	
	public Map<String, IPTCDataSet> getDataSet() {
		return datasetMap;
	}
	
	public boolean isDataLoaded() {
		return loaded;
	}
	
	@Override
	public void read() throws IOException {
		int i = 0;
		int tagMarker = data[i];
		
		while (tagMarker == 0x1c) {
			i++;
			int recordNumber = data[i++]&0xff;
			int tag = data[i++]&0xff;
			int recordSize = IOUtils.readUnsignedShortMM(data, i);
			i += 2;
			IPTCDataSet dataSet = new IPTCDataSet(recordNumber, tag, recordSize, data, i);
			datasetMap.put(dataSet.getName(), dataSet);
			i += recordSize;
			// Sanity check
			if(i >= data.length) break;	
			tagMarker = data[i];							
		}
		
		loaded = true;
	}
	
	public void showMetadata() {
		if(!loaded) {
			try {
				read();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}	
		for(IPTCDataSet iptc : datasetMap.values()) {
			iptc.print();
		}
	}
}
