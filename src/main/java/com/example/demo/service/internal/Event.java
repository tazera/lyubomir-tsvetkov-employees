package com.example.demo.service.internal;

import java.time.LocalDate;

  public final class Event {
    public final LocalDate date;   // timestamp of the event
    public final long employeeId;  // who enters/leaves
    public final boolean enter;    // true = enter, false = leave

    public Event(LocalDate date, long employeeId, boolean enter) {
        this.date = date;
        this.employeeId = employeeId;
        this.enter = enter;
    }
}