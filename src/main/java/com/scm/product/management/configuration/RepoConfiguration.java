package com.scm.product.management.configuration;

import com.scm.product.management.entity.Product;
import com.scm.product.management.entity.Rating;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;


@Configuration
public class RepoConfiguration implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration restConfig) {
        ExposureConfiguration config = restConfig.getExposureConfiguration();
        restConfig.exposeIdsFor(Product.class,Rating.class);
        configureExposure(config,Product.class,HttpMethod.PATCH,HttpMethod.POST,HttpMethod.PUT,HttpMethod.DELETE);
        configureExposure(config, Rating.class,HttpMethod.PATCH,HttpMethod.POST,HttpMethod.PUT,HttpMethod.DELETE);
    }

    private void configureExposure(ExposureConfiguration config,Class<?> resource,HttpMethod... methods){
        config.forDomainType(resource).withItemExposure((metadata, httpMethods) ->
                httpMethods.disable(methods));
        config.forDomainType(resource).withCollectionExposure((metadata, httpMethods) ->
                httpMethods.disable(methods));
        config.forDomainType(resource).withAssociationExposure((metadata, httpMethods) ->
                httpMethods.disable(methods));
    }
}