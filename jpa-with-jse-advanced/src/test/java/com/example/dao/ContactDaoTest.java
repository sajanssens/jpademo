package com.example.dao;

import com.example.domain.Contact;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactDaoTest {

    @Mock Logger log;
    @Mock EntityManager emMock;
    @Mock EntityTransaction entityTransactionMock;

    @InjectMocks
    ContactDao dao = new ContactDao();

    @Test
    void whenInsertIsCalledATransactionIsBegunAndPersistIsCalledAndCommitted() {
        // given
        when(emMock.getTransaction()).thenReturn(entityTransactionMock);
        doNothing().when(entityTransactionMock).begin();
        doNothing().when(entityTransactionMock).commit();

        // when
        dao.save(new Contact());

        // then
        verify(emMock).persist(isA(Contact.class));

        verify(emMock, atLeastOnce()).getTransaction();
        verify(entityTransactionMock).begin();
        verify(entityTransactionMock).commit();
    }

    @Test
    void whenSelectIsCalledFindIsExecutedWithoutTransactionAndResultIsCorrect() {
        // given
        Contact mock = mock(Contact.class);
        when(emMock.find(any(), anyLong())).thenReturn(mock);

        // when
        Contact select = dao.find(1L);

        // then
        verify(emMock).find(any(), eq(1L));

        verify(emMock, never()).getTransaction();
        verify(entityTransactionMock, never()).begin();
        verify(entityTransactionMock, never()).commit();

        assertThat(select).isEqualTo(mock);
    }

    @Mock TypedQuery<Contact> query;
    @Mock Contact e;
    List<Contact> employees = Arrays.asList(e, e, e);

    @Test
    void selectAll() {
        when(emMock.createQuery(anyString(), eq(Contact.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(employees);

        List<Contact> all = dao.findAll();
        assertThat(all.size()).isEqualTo(3);
    }
}
