; Remove Procedures from the Procedure Thing   V1.00     1990   Tony Tebby   QJUMP

        section procs

        xdef    pr_remp

        include 'dev8_keys_err'
        include 'dev8_mac_assert'
        include 'dev8_ee_procs_data'

;+++
; Remove Procedures from the Procedures Thing
;
;       a1 c  p pointer to Thing
;       a2 c  p pointer to extension Thing name
;---
pr_remp
prr.reg reg     a0/a1/a2/a3
        movem.l prr.reg,-(sp)
        bra.s   prr_nimp
prr_exit
        movem.l (sp)+,prr.reg
        rts
prr_nimp
        moveq   #err.nimp,d0
        bra.s   prr_exit
        end
