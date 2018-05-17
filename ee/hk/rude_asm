; HOTKEY rude noise   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_rude

        include 'dev8_keys_qdos_sms'

;+++
; Make Hotkey's standard rude noise.
;
;       status returned according to call value of d0
;---
hk_rude
reglist reg     d0/d5/d7/a3
        movem.l reglist,-(sp)
        lea     ipc_rude,a3              ; make a rude noise
        moveq   #sms.hdop,d0
        trap    #1
        movem.l (sp)+,reglist
        tst.l   d0                       ; restore error
        rts

ipc_rude
        dc.b    $a                       ; sound
        dc.b    8                        ; 8 bytes of parameters
        dc.l    %1010101010101010        ; send all 8 bytes of each
        dc.b    200                      ; pitch1 = 200
        dc.b    0
        dc.b    0,0
        dc.b    0,20                     ; duration = 5120
        dc.b    0,0
        dc.b    1                        ; no reply
        ds.w    0
        end
