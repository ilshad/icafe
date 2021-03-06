package cafe.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cafe.image.meta.Metadata;
import cafe.image.meta.MetadataType;
import cafe.image.meta.iptc.IPTC;
import cafe.image.meta.iptc.IPTCDataSet;

public class TestIPTC {

	public static void main(String[] args) throws IOException {
		Map<MetadataType, Metadata> metadataMap = Metadata.readMetadata(args[0]);
		// Show IPTC specific information
		IPTC iptc = (IPTC)metadataMap.get(MetadataType.IPTC);
		if(iptc != null) {
			// Retrieve a list of Keywords Dataset
			List<IPTCDataSet> keywords = iptc.getDataSet("Keywords");
			//List<IPTCDataset> keywords = iptc.getDataSet(IPTCEnvelopeTag.KEY_WORDS.getName());
			for(IPTCDataSet keyword : keywords)
				keyword.print();
		}
	}
}