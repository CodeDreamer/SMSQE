* Screen Dump V2.00  initialise driver    1987  Tony Tebby   QJUMP
*
        section sdp
*
        xdef    sdp_init
*
        xref    sdp_io
        xref    sdp_open
        xref    sdp_close
        xref    sdp_sched
*
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_qlv'
        include 'dev8_keys_con'
        include 'dev8_sys_sdp_ddlk'
        include 'dev8_smsq_qxl_keys'
        include 'dev8_mac_assert'
*
sdp_parm
        dc.b    0,1,1,1,0,0,0,0         default parameters
        dc.w    3,'SER         '        default device
sdp_pend
*        
proc_tab
        dc.w    4                       four procedures
        xref    sdump
        dc.w    sdump-*
        dc.b    5,'SDUMP'
        xref    sdp_set
        dc.w    sdp_set-*
        dc.b    7,'SDP_SET'
        xref    sdp_key
        dc.w    sdp_key-*
        dc.b    7,'SDP_KEY'
        xref    sdp_dev
        dc.w    sdp_dev-*
        dc.b    7,'SDP_DEV'
        dc.w    0,0,0
*
sdp_init
        lea     proc_tab(pc),a1
        move.w  sb.inipr,a2
        jsr     (a2)
*
        moveq   #sms.achp,d0            allocate space for definition block
        move.l  #sdd_end,d1
        moveq   #0,d2
        trap    #1
        tst.l   d0
        bne.s   sdpi_exit
        move.l  a0,a3
*
        lea     sdp_sched(pc),a2        scheduler
        move.l  a2,sdd_shad(a3)
*
        lea     sdd_ioad(a3),a0
        lea     sdp_io(pc),a2
        move.l  a2,(a0)+                io at $1c
        lea     sdp_open(pc),a2
        move.l  a2,(a0)+                open at $20
        lea     sdp_close(pc),a2
        move.l  a2,(a0)+                close at $24
*
        lea     sdp_parm(pc),a1         copy parameters
        lea     sdd_parm(a3),a2
        moveq   #sdp_pend-sdp_parm-1,d0
sdpi_cpy
        move.b  (a1)+,(a2)+ 
        dbra    d0,sdpi_cpy

        lea     sdd_sinc(a3),a1
        move.l  qxl_swork+qxl_scrb,(a1)+
        move.w  qxl_swork+qxl_scrl,d0
        move.w  d0,(a1)+                ; inc
        lsl.w   #2,d0                   ; x size
        move.w  d0,(a1)+
        move.w  qxl_swork+qxl_scry,(a1)+ ; y size
*
        lea     sdd_shlk(a3),a0         link in scheduler
        moveq   #sms.lshd,d0
        trap    #1
*
        lea     sdd_iolk(a3),a0
        moveq   #sms.liod,d0            link in IO driver
        trap    #1
sdpi_exit
        rts

        end
