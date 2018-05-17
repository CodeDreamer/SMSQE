; Set Priority of my JOB   V2.00    1988  Tony Tebby

        section gen_util

        xdef    gu_spjob

        include 'dev8_keys_qdos_sms'
;+++
; Set Priority of my JOB, preserving registers
;
;       d0 c  r byte priority (-1 = highest) / error code
;       status returned according to D0
;---
gu_spjob
        movem.l d1/d2/a0,-(sp)
        move.b  d0,d2                    ; priority
        bge.s   gsp_do
        moveq   #$7f,d2                  ; max priority
gsp_do
        moveq   #sms.myjb,d1
        moveq   #sms.spjb,d0             ; set priority
        trap    #do.sms2
        movem.l (sp)+,d1/d2/a0
        tst.l   d0
        rts
        end
