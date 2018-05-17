* Check Job ID for validity             v0.00   Feb 1988  J.R.Oakley  QJUMP
*
        section thing
*
        include 'dev8_keys_err'
        include 'dev8_keys_jcb'
        include 'dev8_keys_sys'
*
        xdef    th_chkjb
*+++
* Check that the Job ID passed belongs to a Job currently present
* in the machine: if this is -1, then it means the "current" job, and
* the returned value is this Job's actual Job ID.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0 or IJOB
*       D1      Job ID or -1                    Job ID
*       A0                                      base of job control block
*       A6      system variables                preserved
*---
th_chkjb
        tst.w   d1                      ; is Job ID "myself"?
        bpl.s   thc_chkj                ; no, just check it
        move.l  sys_jbpt(a6),a0         ; yes, get pointer to current job entry
        move.l  a0,d0                   ; keep it
        ble.s   thc_zero                ; SMS2 start up
        move.l  (a0),a0                 ; point to job
        move.w  jcb_tag(a0),d1          ; and get tag
        swap    d1                      ; into MSW
        sub.l   sys_jbtb(a6),d0         ; offset of current job in table
        lsr.l   #2,d0                   ; gives job number
        move.w  d0,d1                   ; thus Job ID
        bra.s   thc_exok

thc_zero
        moveq   #0,d1                   ; job zero
        bra.s   thc_exok

thc_chkj
        cmp.w   sys_jbtp(a6),d1         ; past highest entry?
        bhi.s   thc_exij                ; yes, bad ID
        move.l  d1,d0                   ; get a smashable copy
        add.w   d0,d0                   ; and index...
        add.w   d0,d0                   ; ...into job table
        move.l  sys_jbtb(a6),a0         ; we want...
        move.l  0(a0,d0.w),a0           ; ...this job
        swap    d0
        cmp.w   jcb_tag(a0),d0          ; its tag must match
        bne.s   thc_exij                ; it doesn't
thc_exok
        moveq   #0,d0
thc_exit
        rts
thc_exij
        moveq   #err.ijob,d0
        bra.s   thc_exit
*
        end
