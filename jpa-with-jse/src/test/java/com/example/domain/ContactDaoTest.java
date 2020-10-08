package com.example.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactDaoTest {

    @Mock EntityManager emMock;
    @Mock EntityTransaction entityTransactionMock;

    @InjectMocks
    ContactDao dao = new ContactDao(emMock);

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
