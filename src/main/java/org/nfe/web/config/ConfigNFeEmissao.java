package org.nfe.web.config;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import java.security.cert.CertificateException;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fincatto.nfe310.NFeConfig;
import com.fincatto.nfe310.classes.NFAmbiente;
import com.fincatto.nfe310.classes.NFTipoEmissao;
import com.fincatto.nfe310.classes.NFUnidadeFederativa;
import com.fincatto.nfe310.utils.NFGeraCadeiaCertificados;

@Component
public class ConfigNFeEmissao extends NFeConfig {

	private KeyStore keyStoreCertificado = null;
	private KeyStore keyStoreCadeia = null;

	@Override
	public NFUnidadeFederativa getCUF() {
		return NFUnidadeFederativa.valueOf(System.getProperty("com.fincatto.nfe310.NFeConfig.cUF"));
	}

	@Override
	public String getCertificadoSenha() {
		return System.getProperty("com.fincatto.nfe310.NFeConfig.certificadoSenha");
	}

	@Override
	public String getCadeiaCertificadosSenha() {
		return UUID.randomUUID().toString();
	}

	@Override
	public NFAmbiente getAmbiente() {
		return NFAmbiente.valueOf(System.getProperty("com.fincatto.nfe310.NFeConfig.ambiente", "HOMOLOGACAO"));
	}

	@Override
	public NFTipoEmissao getTipoEmissao() {
		return NFTipoEmissao.EMISSAO_NORMAL;
	}

	@Override
	public KeyStore getCertificadoKeyStore() throws KeyStoreException {
		if (this.keyStoreCertificado == null) {
			this.keyStoreCertificado = KeyStore.getInstance("PKCS12");
			try (InputStream certificadoStream = new FileInputStream(System.getProperty("com.fincatto.nfe310.NFeConfig.certificadoFile"))) {
				this.keyStoreCertificado.load(certificadoStream, this.getCertificadoSenha().toCharArray());
			} catch (CertificateException | NoSuchAlgorithmException | IOException e) {
				this.keyStoreCadeia = null;
				throw new KeyStoreException("Nao foi possibel montar o KeyStore com a cadeia de certificados", e);
			}
		}
		return this.keyStoreCertificado;
	}

	@Override
	public KeyStore getCadeiaCertificadosKeyStore() throws KeyStoreException {
		if (this.keyStoreCadeia == null) {
			byte[] cacerts = null;
			try {
				cacerts = NFGeraCadeiaCertificados.geraCadeiaCertificados(getAmbiente(), getCadeiaCertificadosSenha());
			} catch (Exception e1) {
				throw new KeyStoreException(e1);
			}
			this.keyStoreCadeia = KeyStore.getInstance("JKS");
			try (ByteArrayInputStream in = new ByteArrayInputStream(cacerts)) {
				this.keyStoreCadeia.load(in, this.getCadeiaCertificadosSenha().toCharArray());
			} catch (CertificateException | NoSuchAlgorithmException | IOException e) {
				this.keyStoreCadeia = null;
				throw new KeyStoreException("Nao foi possivel montar o KeyStore com o certificado", e);
			}
		}
		return this.keyStoreCadeia;
	}
}
