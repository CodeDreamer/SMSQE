; Function to set PICK HOTKEY  V2.00    1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_pick

        xref    hot_seti

        include 'dev8_ee_hk_data'

;+++
; Set up a PICK HOTKEY
;
; error = HOT_PICK (key,name)
;---
hot_pick
        moveq   #hki.pick,d6             ; set pick
        jmp     hot_seti                 ; using utility
        end
