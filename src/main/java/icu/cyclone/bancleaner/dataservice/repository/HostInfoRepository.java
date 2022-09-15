package icu.cyclone.bancleaner.dataservice.repository;

import icu.cyclone.bancleaner.domain.HostInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostInfoRepository extends CrudRepository<HostInfo, String> {
}
