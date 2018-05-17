; Kill or Die    V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_rjob
        xdef    gu_die

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_err'
;+++
; Called when program wishes to die: standard force remove job
;
;       d0 c    error code
;---
gu_die
        moveq   #sms.myjb,d1             ; remove myself
;+++
; Standard force remove job
;
;       d0 cr   error code
;       d1 c s  job ID
;---
gu_rjob
grj.reg reg     d2/d3/d4/d5/d6/a0/a1/a2/a3/a4
        movem.l grj.reg,-(sp)
        move.l  d0,d3                    ; error return
        add.l   d0,d0                    ; standard error?
        bvc.s   grj_rjob
        moveq   #err.nc,d3               ; ... no, make it standard
grj_rjob
        moveq   #sms.frjb,d0             ; remove
        trap    #do.sms2
        movem.l (sp)+,grj.reg
        rts
        end
