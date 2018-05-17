; Procedure to go and stop Hotkey    V2.00     1990   Tony Tebby   QJUMP

        section hotkey

        xdef    hxt_go
        xdef    hxt_stop

        xref    hxt_set

        xref    hk_cjob
        xref    hk_kjob

        xref    thp_nul

        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_keys_thg'
        include 'dev8_ee_hk_data'
        include 'dev8_mac_thg'

;+++
; HOT_GO
;---
hxt_go thg_extn {GO  },hxt_stop,thp_nul

hgs.reg reg     a1/a3
        movem.l hgs.reg,-(sp)
        lea     hk_cjob,a1               ; create job
        bra.s   hgs_do

;+++
; HOT_STOP
;---
hxt_stop thg_extn {STOP},hxt_set,thp_nul
        movem.l hgs.reg,-(sp)
        lea     hk_kjob,a1               ; kill job

hgs_do
        jsr     gu_hkuse                 ; use hotkey
        bne.s   hgs_exit
        jsr     (a1)
        jsr     gu_hkfre

hgs_exit
        movem.l (sp)+,hgs.reg
        rts
        end
