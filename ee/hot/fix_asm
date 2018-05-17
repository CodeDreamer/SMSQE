; Fix the THING system  V0.00      1988   Tony Tebby   QJUMP

        section thing

        xdef    th_fix

        xref    cv_lostr
        xref    th_enfix
        xref    th_exfix

        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'

;+++
; Fix the Thing system to the old type (SuperBASIC call)
;
;       status return 0 ok
;---
th_fix
        move.w  sr,d7
        trap    #0
        moveq   #sms.info,d0             ; get system information
        trap    #1
        move.l  sys_thgl(a0),a0          ; Thing list

thf_loop
        lea     th_name(a0),a1           ; name
        jsr     cv_lostr                 ; lowercased
        move.l  (a0),a0                  ; another thing
        tst.l   (a0)                     ; last thing?
        bne.s   thf_loop                 ; ... no, next thing

thf_done
        move.l  th_thing(a0),a0          ; the thing thing
        lea     th_enfix,a1
        move.l  a1,thh_entr(a0)          ; set new entry vector
        lea     th_exfix,a1
        move.l  a1,thh_exec(a0)          ; and new exec vector

        move.w  d7,sr
        moveq   #0,d0
        rts

        end
