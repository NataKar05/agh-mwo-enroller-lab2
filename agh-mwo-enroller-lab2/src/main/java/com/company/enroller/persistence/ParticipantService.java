package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
        String hql = "FROM Participant";
        boolean whereAdded = false;

        // Filtrowanie po loginie
        if (key != null && !key.isEmpty()) {
            hql += " WHERE login LIKE :key";
            whereAdded = true;
        }

        // Sortowanie tylko po loginie
        if ("login".equalsIgnoreCase(sortBy)) {
            hql += " ORDER BY login";
            if ("DESC".equalsIgnoreCase(sortOrder)) {
                hql += " DESC";
            } else {
                hql += " ASC"; // domyślnie ASC jeśli nie podano sortOrder
            }
        }

        Query query = connector.getSession().createQuery(hql);

        if (key != null && !key.isEmpty()) {
            query.setParameter("key", "%" + key + "%");
        }

        return query.list();
    }

    public Participant findByLogin(String login) {
        return connector.getSession().get(Participant.class, login);
    }

    public Participant add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
        return participant;
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }
}