//package com.piccologrupa.zamjenasoba.jwt;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.session.SessionInformation;
//import org.springframework.security.core.session.SessionRegistry;
//
//public class SesssionRegistryImpl implements SessionRegistry {
//
//	@Autowired
//	private SessionRegistry sessionRegistry;
//	
//	@Override
//	public List<Object> getAllPrincipals() {
//		return sessionRegistry.getAllPrincipals().stream()
//			      .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
//			      .map(Object::toString)
//			      .collect(Collectors.toList());
//	}
//
//	@Override
//	public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public SessionInformation getSessionInformation(String sessionId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void refreshLastRequest(String sessionId) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void registerNewSession(String sessionId, Object principal) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void removeSessionInformation(String sessionId) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
