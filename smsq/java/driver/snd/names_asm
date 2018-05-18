; the basic name defintions for soundfile et al  v. 1.00 (c) 2012 W. Lenerz


	section sound


	include 'dev8_mac_proc'
	xdef	snd_nam

	section procs

snd_nam lea	procs,a1
	move.w	$110,a2
	jmp	(a2)
procs
	proc_stt
	proc_def	SOUNDFILE
	proc_def	SOUNDFILE2
	proc_def	SOUNDFILE3
	proc_def	SETRATE10
	proc_def	SETRATE20
	proc_def	SOUNDSAMPLE2
	proc_def	SOUNDSAMPLE3
	proc_def	KILLSOUND
	proc_def	JVAVOL
	proc_def	JVA_VOL,jvavol
	proc_end
	proc_stt
	proc_def	SOUNDSAMPLE
	proc_end


	end
