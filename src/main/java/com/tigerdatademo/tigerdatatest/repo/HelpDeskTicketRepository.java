package com.tigerdatademo.tigerdatatest.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tigerdatademo.tigerdatatest.entity.HelpDeskTicket;


public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket, Long> {

    List<HelpDeskTicket> findByUsername(String username);
}
