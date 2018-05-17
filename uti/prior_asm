; Set Priority of my JOB   V2.00    1988  Tony Tebby

; return in D1 real Job-ID !!

        section utility

        xdef    ut_prior

        include 'dev8_keys_qdos_sms'
;+++
; Set Priority of my JOB, preserving registers
;
;       d0 c  r byte priority (-1 = highest) / error code
;       d1      real job-id
;
;       status returned according to D0
;---
ut_prior
        movem.l d2/a0,-(sp)
        move.b  d0,d2                    ; priority
        bge.s   gsp_do
        moveq   #$7f,d2                  ; max priority
gsp_do
        moveq   #sms.myjb,d1
        moveq   #sms.spjb,d0             ; set priority
        trap    #do.sms2
        movem.l (sp)+,d2/a0
        tst.l   d0
        rts
        end
