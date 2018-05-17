* SMS Job Utilities   V2.00    1986   Tony Tebby  QJUMP
*
        section sms
*
        xdef    sms_ckid        check job id is possible
        xdef    sms_ckjx        check job exists / set address in a0
        xdef    sms_cjid        get current job ID / base address
*
        include dev8_keys_err
        include dev8_keys_sys
        include dev8_keys_jcbq
*
*       d0  r   0 or invalid job
*       d1 cr   job ID
*       a0  r   base address of job
*
sms_ckid
        move.l  d1,d0                   job 0?
        bne.s   sms_ckjx                ... no
        move.l  sys_jbtb(a6),a0         ... yes
        move.l  (a0),a0
        rts

sms_ckjx
        tst.w   d1                      unset job ID?
        bmi.s   sms_cjid                yes, get current job ID
*
        cmp.w   sys_jbtp(a6),d1         job number to large?
        bhi.s   scj_ijob                ... yes
        move.w  d1,d0
        lsl.w   #2,d0                   index table in long words
        move.l  sys_jbtb(a6),a0         base of job table
        move.l  (a0,d0.w),d0            job address
        bmi.s   scj_ijob                ... unset
        move.l  d0,a0                   ... set
        move.l  d1,d0                   get tag
        swap    d0                      check tag
        cmp.w   jcb_tag(a0),d0
        bne.s   scj_ijob                ... does not match
scj_ok
        moveq   #0,d0
        rts
*
scj_ijob
        moveq  #err.ijob,d0
        rts

; get current job ID

sms_cjid
        move.l  sys_jbpt(a6),a0          ; pointer to table
        move.l  a0,d0
        sub.l   sys_jbtb(a6),d0          ; less base of table
        bmi.s   scj_zero                 ; ... no job at all!
        lsr.w   #2,d0                    ; /4 is job number
        move.l  (a0),a0                  ; address of job
        move.l  jcb_tag(a0),d1           ; set tag, smash number
        move.w  d0,d1                    ; set number
        moveq   #0,d0                    ; no error
        rts

scj_zero
        moveq   #0,d1                    ; job zero
        moveq   #0,d0                    ; must be OK
        rts
        end
