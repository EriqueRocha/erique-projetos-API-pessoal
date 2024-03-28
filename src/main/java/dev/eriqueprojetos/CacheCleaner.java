package dev.eriqueprojetos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
public class CacheCleaner {

    private boolean limpezaJaRealizada = false;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory; // Injeta o EntityManagerFactory para acesso ao cache do JPA

    @Scheduled(fixedRate = 60000) // Verifica a cada minuto se a limpeza deve ser realizada
    public void verificaNecessidadeDeLimpeza() {
        if (!limpezaJaRealizada) {
            // Limpa o cache se não foi limpo recentemente e não houve atividade
            limpaCache();
        }
    }

    private void limpaCache() {
        // Limpa o cache em memória
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());

        // Limpa o cache de segundo nível do JPA
        entityManagerFactory.getCache().evictAll();

        limpezaJaRealizada = true;
    }

}
