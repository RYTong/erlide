package com.rytong.ui.formatter;

import java.io.UnsupportedEncodingException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.erlide.backend.BackendCore;
import org.erlide.backend.IBackend;
import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.RpcException;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;

/**
 * @author lu.jingbo@rytong.com
 * 
 */
public class ConfFormatter implements IFormatter {
	private IDocument document;

	public ConfFormatter() {
	}

	public ConfFormatter(IDocument document) {
		this.document = document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rytong.ui.formatter.IFormatter#format(java.lang.String, int,
	 * int)
	 */
	@Override
	public void format(String source, int offset, int length) {
		try {
    	String s = new String(source.getBytes("utf-8"), "ISO-8859-1");
			IBackend ideBackend = BackendCore.getBackendManager()
					.getIdeBackend();
			if (ideBackend != null) {
				ErlLogger.debug("call erlang backend to format conf file");
				OtpErlangObject res = null;
				res = ideBackend.call("conf_prettypr", "format", "s", s);
				if (res instanceof OtpErlangTuple) {
					final OtpErlangTuple tuple = (OtpErlangTuple) res;
					ErlLogger.debug("the tuple : " + tuple);
					OtpErlangAtom tag = (OtpErlangAtom) tuple.elementAt(0);

					if (tag.atomValue().equals("ok")) {
						String formatedSource = ((OtpErlangString) tuple
								.elementAt(1)).stringValue();
                        String s2 = new String(formatedSource.getBytes("ISO-8859-1"), "utf-8");
						TextEdit te = new ReplaceEdit(offset, length, s2);
						te.apply(document);
					}
				}
			}
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rytong.ui.formatter.IFormatter#getDocument()
	 */
	@Override
	public IDocument getDocument() {
		return document;
	}

}
