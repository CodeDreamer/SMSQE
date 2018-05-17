; Procedure to go and stop Hotkey    V2.00     1990   Tony Tebby   QJUMP

        section hotkey

        xdef    hxt_set
        xdef    hxt_on
        xdef    hxt_off

        xref    hxt_remv

        xref    thp_str
        xref    hxt_prks

        xref    hk_set

        xref    gu_hkuse
        xref    gu_hkfre

        include 'dev8_keys_thg'
        include 'dev8_ee_hk_data'
        include 'dev8_ee_hk_vector'
        include 'dev8_mac_thg'

;+++
; HOT_SET key, key / name
;---
hxt_set thg_extn {SET },hxt_on,hxt_prks

hst.reg reg     d1/d6/a1/a3
        movem.l hst.reg,-(sp)

        moveq   #hks.rset,d6             ; reset action
        move.l  (a1)+,d1                 ; hotkey
        bra.s   hst_do

;+++
; HOT_ON key / name
;---
hxt_on  thg_extn {ON  },hxt_off,thp_str

        movem.l hst.reg,-(sp)

        moveq   #hks.on,d6               ; on action
        bra.s   hst_do
;+++
; HOT_OFF key / name
;---
hxt_off thg_extn {OFF },hxt_remv,thp_str
        movem.l hst.reg,-(sp)
        moveq   #hks.off,d6              ; off action

hst_do
        move.l  4(a1),a1                 ; name / key
        jsr     gu_hkuse                 ; use hotkey
        bne.s   hst_exit

        move.l  d6,d0                    ; action
        jsr     hk_set                   ; set/reset hotkey

        jsr     gu_hkfre

hst_exit
        movem.l (sp)+,hst.reg
        rts
        end
