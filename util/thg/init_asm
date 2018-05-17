; Initialise the Thing system  1988   Tony Tebby   QJUMP

        section base

        xdef    thg_init

        xref.l  th_vers

        xref    th_entry
        xref    th_exec
        xref    gu_achpp

        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'
        include 'dev8_ee_hk_data'
        include 'dev8_mac_assert'

        bra.s   thg_init
        dc.b    ' ',$a

        section version

        dc.w    'THING System V'
        dc.l    th_vers
        dc.b    ' ',$a

        section thing

;+++
; Initialise thing system
;---
thg_init
        trap    #0
        moveq   #sms.info,d0             ; get system information
        trap    #1
        move.l  a0,a3                    ; save it

        tst.l   sys_thgl(a3)             ; thing already present?
        bne.s   thi_rte                  ; ... yes,
        moveq   #hkd.thg+$18,d0          ; allocate thing itself
        jsr     gu_achpp
        bne.s   thi_rte
        move.l  a0,sys_thgl(a3)          ; point to it
        lea     hkd.thg(a0),a2
        move.l  a2,th_thing(a0)          ; point to vector
        lea     th_verid(a0),a0
        move.l  #th_vers,(a0)+           ; set version
        move.w  #5,(a0)+                 ; and name
        move.l  #'THIN',(a0)+
        move.b  #'G',(a0)+

        move.l  #'THG%',(a2)+            ; ID
        subq.l  #1,(a2)+                 ; type
        lea     th_entry,a0
        move.l  a0,(a2)+                 ; th_entry
        lea     th_exec,a0
        move.l  a0,(a2)+                 ; th_exec

thi_rte
        and.w   #$dfff,sr
        tst.l   d0
        rts
        end
