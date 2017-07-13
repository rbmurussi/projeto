package org.nfe.web.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import java.security.cert.CertificateException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.fincatto.nfe310.NFeConfig;
import com.fincatto.nfe310.classes.NFAmbiente;
import com.fincatto.nfe310.classes.NFTipoEmissao;
import com.fincatto.nfe310.classes.NFUnidadeFederativa;
import com.fincatto.nfe310.utils.NFGeraCadeiaCertificados;

@Component
public class NFeConfigEmissor extends NFeConfig {

    private KeyStore keyStoreCertificado = null;
    private KeyStore keyStoreCadeia = null;

    @Override
    public NFUnidadeFederativa getCUF() {
    	return NFUnidadeFederativa.valueOf(System.getProperty("unidade_federativa"));
    }

    @Override
    public String getCertificadoSenha() {
        return System.getProperty("senha_certificado");
    }

    @Override
    public String getCadeiaCertificadosSenha() {
    	return "senha_cacerts";
    }
    
    @Override
    public NFAmbiente getAmbiente() {
    	return NFAmbiente.valueOf(System.getProperty("ambiente","HOMOLOGACAO"));
    }

    @Override  
    public NFTipoEmissao getTipoEmissao() {  
        return NFTipoEmissao.EMISSAO_NORMAL;  
    }
    
    @Override
    public KeyStore getCertificadoKeyStore() throws KeyStoreException {
        if (this.keyStoreCertificado == null) {
            this.keyStoreCertificado = KeyStore.getInstance("PKCS12");
            try (InputStream certificadoStream = new FileInputStream(System.getProperty("arquivo_certificado_pfx"))) {
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
        	File fileCacertsJKS = new File(System.getProperty("arquivo_certificado_pfx")).getParentFile();
    		if(!fileCacertsJKS.exists()) {
    			throw new KeyStoreException("Diretorio não existe: " + fileCacertsJKS.getAbsolutePath());
    		}
    		
        	if(NFAmbiente.HOMOLOGACAO.equals(getAmbiente())) {
        		fileCacertsJKS = new File(fileCacertsJKS.getAbsolutePath() + File.separator + "homologacao.cacerts.jks");
        	} else if(NFAmbiente.PRODUCAO.equals(getAmbiente())) {
        		fileCacertsJKS = new File(fileCacertsJKS.getAbsolutePath() + File.separator + "producao.cacerts.jks");
        	} else {
        		throw new KeyStoreException("Variavel de ambiente NFAmbiente não definida corretamente!");
        	}
        	// A cada nova atualização os cacerts são atualizados...
        	if(!fileCacertsJKS.exists()) {
        		fileCacertsJKS.delete();
        	}
        	try {
    			FileUtils.writeByteArrayToFile(fileCacertsJKS, NFGeraCadeiaCertificados.geraCadeiaCertificados(getAmbiente(), getCadeiaCertificadosSenha()));
    		} catch (Exception e1) {
    			throw new KeyStoreException("Erro ao criar arquivo cacerts JKS!", e1);
    		}
        	
            this.keyStoreCadeia = KeyStore.getInstance("JKS");
            try (InputStream cadeia = new FileInputStream(fileCacertsJKS)) {
                this.keyStoreCadeia.load(cadeia, this.getCadeiaCertificadosSenha().toCharArray());
            } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
                this.keyStoreCadeia = null;
                throw new KeyStoreException("Nao foi possibel montar o KeyStore com o certificado", e);
            }
        }
        return this.keyStoreCadeia;
    }
}
