; List Procedures in the Procedure Thing   V1.00     1990   Tony Tebby   QJUMP

        section procs

        xdef    pr_mkls

        include 'dev8_keys_err'
        include 'dev8_mac_assert'
        include 'dev8_ee_procs_data'

;+++
; List Procedures in the Procedures Thing
;
;       d0 c    entry length
;       d1  r   number of entries
;       a1 cr   pointer to procedures Thing / pointer to list
;       a2 c  p pointer to extension Thing name or zero
;---
pr_mkls
prl.reg reg     a0/a1/a2/a3
        movem.l prl.reg,-(sp)
        bra.s   prl_nimp
prl_exit
        movem.l (sp)+,prl.reg
        rts
prl_nimp
        moveq   #err.nimp,d0
        bra.s   prl_exit
        end
